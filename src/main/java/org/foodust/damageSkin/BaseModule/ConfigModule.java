package org.foodust.damageSkin.BaseModule;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.foodust.damageSkin.DamageSkin;
import org.foodust.damageSkin.Data.AnimateData;
import org.foodust.damageSkin.Data.SkinData;
import org.foodust.damageSkin.Data.TaskData;
import org.foodust.damageSkin.Data.info.SkinInfo;
import org.foodust.damageSkin.Message.BaseMessage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ConfigModule {
    private final DamageSkin plugin;
    private final MessageModule messageModule;

    public ConfigModule(DamageSkin plugin) {
        this.plugin = plugin;
        this.messageModule = new MessageModule(plugin);
    }

    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            // 파일이 존재하지 않으면 기본 설정을 생성
            plugin.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig(File configFile) {
        if (!configFile.exists()) {
            // 파일이 존재하지 않으면 기본 설정을 생성
            plugin.saveResource(configFile.getName(), false);
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig(FileConfiguration config, String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        try {
            config.save(configFile);
        } catch (IOException e) {
            Bukkit.getLogger().info(e.getMessage());
        }
    }

    public void release() {
        TaskData.release();
        AnimateData.release();
        SkinData.release();
    }

    public void initialize() {
        // 1. skin 폴더의 모든 yml 파일 로드
        loadSkinConfigs();

        // 2. player UUID 폴더의 yml 파일 로드
        loadPlayerSkins();
    }

    private void loadSkinConfigs() {
        File skinFolder = new File(plugin.getDataFolder(), "skin");
        if (!skinFolder.exists()) {
            boolean mkdirs = skinFolder.mkdirs();
        }

        File[] skinFiles = skinFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (skinFiles == null) return;
        for (File file : skinFiles) {
            String skinName = file.getName().replace(".yml", "");
            FileConfiguration config = getConfig(file);

            SkinInfo skinInfo = loadSkinInfoFromConfig(config);
            SkinData.skins.put(skinName, skinInfo);
        }
    }

    private void loadPlayerSkins() {
        File playerFolder = new File(plugin.getDataFolder(), "player");
        if (!playerFolder.exists()) {
            boolean mkdirs = playerFolder.mkdirs();
        }
        File[] playerFiles = playerFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (playerFiles == null)
            return;

        for (File file : playerFiles) {
            String uuidStr = file.getName().replace(".yml", "");
            try {
                UUID uuid = UUID.fromString(uuidStr);
                FileConfiguration config = getConfig(file);
                // skinName을 가져옴
                String skinName = config.getString("skinName", "basic");
                // skins HashMap에서 해당 스킨 정보를 가져옴
                SkinInfo skinInfo = SkinData.skins.get(skinName);
                // 만약 해당 스킨이 없다면 기본 스킨(basic)을 사용
                if (skinInfo == null) {
                    messageModule.logInfo(BaseMessage.ERROR_NO_SKIN.getMessage() + skinName);
                    continue;
                }
                SkinData.playerSkins.put(uuid, skinInfo);
            } catch (IllegalArgumentException e) {
                messageModule.logWarning(BaseMessage.ERROR_NO_PLAYER.getMessage());
            }
        }
    }

    private SkinInfo loadSkinInfoFromConfig(FileConfiguration config) {
        SkinInfo.SkinInfoBuilder builder = SkinInfo.builder();

        // Billboard 설정
        String billboardStr = config.getString("billboard", "FIXED");
        try {
            builder.billboard(Display.Billboard.valueOf(billboardStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            builder.billboard(Display.Billboard.FIXED);
        }

        // 기본 값 설정
        builder.duration(config.getLong("duration", 0));
        builder.speed(config.getDouble("location.speed", 0));

        // Location Vector 설정
        double locX = config.getDouble("location.x", 0);
        double locY = config.getDouble("location.y", 0);
        double locZ = config.getDouble("location.z", 0);
        builder.location(new Vector(locX, locY, locZ));

        // Size Vector 설정
        double sizeX = config.getDouble("size.x", 0);
        double sizeY = config.getDouble("size.y", 0);
        double sizeZ = config.getDouble("size.z", 0);
        builder.size(new Vector(sizeX, sizeY, sizeZ));

        // Characters HashMap 설정
        HashMap<String, String> characters = new HashMap<>();
        ConfigurationSection charsSection = config.getConfigurationSection("characters");
        if (charsSection != null) {
            for (String key : charsSection.getKeys(false)) {
                characters.put(key, charsSection.getString(key, ""));
            }
        }
        builder.characters(characters);
        return builder.build();
    }

    public void commandSet(CommandSender sender, String[] data) {
        if (data.length < 3) {
            messageModule.sendPlayerC(sender, BaseMessage.ERROR_WRONG_COMMAND.getMessage());
            return;
        }
        String playerName = data[1];

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            messageModule.sendPlayerC(sender, BaseMessage.ERROR_NO_PLAYER.getMessage());
            return;
        }
        String skinName = data[2];
        if (!SkinData.skins.containsKey(skinName)) {
            messageModule.sendPlayerC(sender, BaseMessage.ERROR_NO_SKIN.getMessage() + skinName);
            return;
        }
        UUID uniqueId = player.getUniqueId();

        SkinData.playerSkins.put(uniqueId, SkinData.skins.get(skinName));

        // player/{UUID}.yml 파일 생성 및 저장
        File playerFolder = new File(plugin.getDataFolder(), "player");
        if (!playerFolder.exists()) {
            boolean mkdirs = playerFolder.mkdirs();
        }
        String fileName = "player/" + uniqueId + ".yml";
        FileConfiguration config = new YamlConfiguration();
        config.set("skinName", skinName);
        config.set("playerName", player.getName());
        saveConfig(config, fileName);
        // 성공 메시지 전송
        messageModule.sendPlayerC(sender, BaseMessage.INFO_SET_SKIN.getMessage());
    }

    public void commandRemove(CommandSender sender, String[] data) {
        if (data.length < 2) {
            messageModule.sendPlayerC(sender, BaseMessage.ERROR_WRONG_COMMAND.getMessage());
            return;
        }

        String playerName = data[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            messageModule.sendPlayerC(sender, BaseMessage.ERROR_NO_PLAYER.getMessage());
            return;
        }

        UUID uniqueId = player.getUniqueId();

        // 메모리에서 제거
        SkinData.playerSkins.remove(uniqueId);
        // 파일 제거
        File playerFile = new File(plugin.getDataFolder(), "player/" + uniqueId + ".yml");
        if (playerFile.exists()) {
            boolean deleted = playerFile.delete();
            if (!deleted) {
                messageModule.logWarning(BaseMessage.ERROR_ALREADY_DELETE.getMessage() + playerName);
            }
        }
        // 성공 메시지 전송
        messageModule.sendPlayerC(sender, playerName + BaseMessage.INFO_REMOVE_SKIN.getMessage());
    }

    public void commandReload(CommandSender sender, String[] data) {
        initialize();
        messageModule.sendPlayerC(sender, BaseMessage.INFO_RELOAD.getMessage());
    }

}
