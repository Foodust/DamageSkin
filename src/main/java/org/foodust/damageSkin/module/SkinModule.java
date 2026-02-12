package org.foodust.damageSkin.module;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.foodust.damageSkin.DamageSkin;
import org.foodust.damageSkin.message.BaseMessage;

import java.io.File;
import java.util.*;

@Getter
public class SkinModule extends BaseModule implements Listener {

    private final Map<String, SkinInfo> skins = new HashMap<>();
    private final Map<UUID, SkinInfo> playerSkins = new HashMap<>();
    private final List<BukkitTask> tasks = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();

    public SkinModule(DamageSkin plugin) {
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        loadSkinConfigs();
        loadPlayerSkins();
    }

    public void release() {
        tasks.forEach(task -> {
            if (task != null) {
                Bukkit.getScheduler().cancelTask(task.getTaskId());
            }
        });
        tasks.clear();

        entities.forEach(entity -> {
            if (entity != null) {
                entity.remove();
            }
        });
        entities.clear();

        skins.clear();
        playerSkins.clear();
    }

    private void loadSkinConfigs() {
        skins.clear();
        File skinFolder = new File(plugin.getDataFolder(), "skin");
        if (!skinFolder.exists()) {
            skinFolder.mkdirs();
        }

        File[] skinFiles = skinFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (skinFiles == null) return;

        for (File file : skinFiles) {
            String skinName = file.getName().replace(".yml", "");
            FileConfiguration skinConfig = YamlConfiguration.loadConfiguration(file);
            SkinInfo skinInfo = loadSkinInfoFromConfig(skinConfig);
            skins.put(skinName, skinInfo);
        }
    }

    private void loadPlayerSkins() {
        playerSkins.clear();
        File playerFolder = new File(plugin.getDataFolder(), "player");
        if (!playerFolder.exists()) {
            playerFolder.mkdirs();
        }

        File[] playerFiles = playerFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (playerFiles == null) return;

        for (File file : playerFiles) {
            String uuidStr = file.getName().replace(".yml", "");
            try {
                UUID uuid = UUID.fromString(uuidStr);
                FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
                String skinName = playerConfig.getString("skinName", "basic");
                SkinInfo skinInfo = skins.get(skinName);
                if (skinInfo == null) {
                    plugin.getLogger().info(BaseMessage.ERROR_NO_SKIN.getMessage() + skinName);
                    continue;
                }
                playerSkins.put(uuid, skinInfo);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning(BaseMessage.ERROR_NO_PLAYER.getMessage());
            }
        }
    }

    private SkinInfo loadSkinInfoFromConfig(FileConfiguration config) {
        SkinInfo.SkinInfoBuilder builder = SkinInfo.builder();

        String billboardStr = config.getString("billboard", "FIXED");
        try {
            builder.billboard(Display.Billboard.valueOf(billboardStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            builder.billboard(Display.Billboard.FIXED);
        }

        builder.duration(config.getLong("duration", 0));
        builder.speed(config.getDouble("location.speed", 0));

        double locX = config.getDouble("location.x", 0);
        double locY = config.getDouble("location.y", 0);
        double locZ = config.getDouble("location.z", 0);
        builder.location(new Vector(locX, locY, locZ));

        double sizeX = config.getDouble("size.x", 0);
        double sizeY = config.getDouble("size.y", 0);
        double sizeZ = config.getDouble("size.z", 0);
        builder.size(new Vector(sizeX, sizeY, sizeZ));

        double minX = config.getDouble("random.min.x", 0);
        double minY = config.getDouble("random.min.y", 0);
        double minZ = config.getDouble("random.min.z", 0);
        builder.minRandom(new Vector(minX, minY, minZ));

        double maxX = config.getDouble("random.max.x", 0);
        double maxY = config.getDouble("random.max.y", 0);
        double maxZ = config.getDouble("random.max.z", 0);
        builder.maxRandom(new Vector(maxX, maxY, maxZ));

        Map<String, String> characters = new HashMap<>();
        ConfigurationSection charsSection = config.getConfigurationSection("characters");
        if (charsSection != null) {
            for (String key : charsSection.getKeys(false)) {
                characters.put(key, charsSection.getString(key, ""));
            }
        }
        builder.characters(characters);

        return builder.build();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof Player player)) return;
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) return;

        UUID uuid = player.getUniqueId();
        SkinInfo skinInfo = playerSkins.get(uuid);
        if (skinInfo == null) return;

        double damage = event.getDamage();
        String damageText = convertDamageToCustomText(skinInfo, damage);

        Vector minRandom = skinInfo.getMinRandom();
        Vector maxRandom = skinInfo.getMaxRandom();
        Random random = new Random();

        double randomX = minRandom.getX() + random.nextDouble() * (maxRandom.getX() - minRandom.getX());
        double randomY = minRandom.getY() + random.nextDouble() * (maxRandom.getY() - minRandom.getY());
        double randomZ = minRandom.getZ() + random.nextDouble() * (maxRandom.getZ() - minRandom.getZ());

        BoundingBox boundingBox = entity.getBoundingBox();
        Vector center = boundingBox.getCenter();
        Location entityLocation = new Location(
                player.getWorld(),
                center.getX() + randomX,
                center.getY() + livingEntity.getEyeHeight() + randomY,
                center.getZ() + randomZ
        );

        TextDisplay textDisplay = makeTextDisplay(entity, entityLocation, damageText, skinInfo.getSize(), skinInfo.getBillboard());
        textDisplay.setDefaultBackground(false);
        textDisplay.setShadowed(false);
        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));

        long duration = skinInfo.getDuration();
        double speed = skinInfo.getSpeed();
        Vector moveVector = skinInfo.getLocation().clone();

        BukkitTask moveTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Location currentDisplayLoc = textDisplay.getLocation().clone();
            currentDisplayLoc.add(
                    moveVector.getX() * speed,
                    moveVector.getY() * speed,
                    moveVector.getZ() * speed
            );
            textDisplay.teleport(currentDisplayLoc);
        }, 0L, 1L);
        tasks.add(moveTask);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.getScheduler().cancelTask(moveTask.getTaskId());
            tasks.remove(moveTask);
            textDisplay.remove();
            entities.remove(textDisplay);
        }, duration);
    }

    private String convertDamageToCustomText(SkinInfo skinInfo, double damage) {
        String damageStr = String.valueOf((int) damage);
        StringBuilder result = new StringBuilder();

        for (char digit : damageStr.toCharArray()) {
            String digitStr = String.valueOf(digit);
            String customChar = skinInfo.getCharacters().getOrDefault(digitStr, digitStr);
            result.append(customChar);
        }
        return result.toString();
    }

    private TextDisplay makeTextDisplay(Entity entity, Location location, String text, Vector size, Display.Billboard billboard) {
        TextDisplay textDisplay = entity.getWorld().spawn(location, TextDisplay.class);
        textDisplay.setText(text);
        textDisplay.setBillboard(billboard);
        Transformation transformation = textDisplay.getTransformation();
        transformation.getScale().set(size.getX(), size.getY(), size.getZ());
        textDisplay.setTransformation(transformation);
        entities.add(textDisplay);
        return textDisplay;
    }

    public void commandSet(CommandSender sender, String[] data) {
        if (data.length < 3) {
            sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.ERROR_WRONG_COMMAND.getMessage());
            return;
        }

        String playerName = data[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.ERROR_NO_PLAYER.getMessage());
            return;
        }

        String skinName = data[2];
        if (!skins.containsKey(skinName)) {
            sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.ERROR_NO_SKIN.getMessage() + skinName);
            return;
        }

        UUID uniqueId = player.getUniqueId();
        playerSkins.put(uniqueId, skins.get(skinName));

        File playerFolder = new File(plugin.getDataFolder(), "player");
        if (!playerFolder.exists()) {
            playerFolder.mkdirs();
        }

        String fileName = "player/" + uniqueId + ".yml";
        File configFile = new File(plugin.getDataFolder(), fileName);
        FileConfiguration playerConfig = new YamlConfiguration();
        playerConfig.set("skinName", skinName);
        playerConfig.set("playerName", player.getName());

        try {
            playerConfig.save(configFile);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save player config: " + e.getMessage());
        }

        sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.INFO_SET_SKIN.getMessage());
    }

    public void commandSetAll(CommandSender sender, String[] data) {
        if (data.length < 2) {
            sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.ERROR_WRONG_COMMAND.getMessage());
            return;
        }

        String skinName = data[1];
        if (!skins.containsKey(skinName)) {
            sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.ERROR_NO_SKIN.getMessage() + skinName);
            return;
        }

        SkinInfo skinInfo = skins.get(skinName);
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uniqueId = player.getUniqueId();
            playerSkins.put(uniqueId, skinInfo);

            File playerFolder = new File(plugin.getDataFolder(), "player");
            if (!playerFolder.exists()) {
                playerFolder.mkdirs();
            }

            String fileName = "player/" + uniqueId + ".yml";
            File configFile = new File(plugin.getDataFolder(), fileName);
            FileConfiguration playerConfig = new YamlConfiguration();
            playerConfig.set("skinName", skinName);
            playerConfig.set("playerName", player.getName());

            try {
                playerConfig.save(configFile);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to save player config: " + e.getMessage());
            }
        }

        sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.INFO_SET_ALL_SKIN.getMessage());
    }

    public void commandRemove(CommandSender sender, String[] data) {
        if (data.length < 2) {
            sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.ERROR_WRONG_COMMAND.getMessage());
            return;
        }

        String playerName = data[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.ERROR_NO_PLAYER.getMessage());
            return;
        }

        UUID uniqueId = player.getUniqueId();
        playerSkins.remove(uniqueId);

        File playerFile = new File(plugin.getDataFolder(), "player/" + uniqueId + ".yml");
        if (playerFile.exists()) {
            if (!playerFile.delete()) {
                plugin.getLogger().warning(BaseMessage.ERROR_ALREADY_DELETE.getMessage() + playerName);
            }
        }

        sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + playerName + BaseMessage.INFO_REMOVE_SKIN.getMessage());
    }

    public void commandReload(CommandSender sender) {
        reloadConfig();
        sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.INFO_RELOAD.getMessage());
    }

    @lombok.Getter
    @lombok.Setter
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class SkinInfo {
        @lombok.Builder.Default
        private Display.Billboard billboard = Display.Billboard.FIXED;

        @lombok.Builder.Default
        private long duration = 0;

        @lombok.Builder.Default
        private double speed = 0;

        @lombok.Builder.Default
        private Vector location = new Vector(0, 0, 0);

        @lombok.Builder.Default
        private Vector size = new Vector(0, 0, 0);

        @lombok.Builder.Default
        private Vector minRandom = new Vector(0, 0, 0);

        @lombok.Builder.Default
        private Vector maxRandom = new Vector(0, 0, 0);

        @lombok.Builder.Default
        private Map<String, String> characters = new HashMap<>();
    }
}
