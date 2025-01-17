package org.foodust.damageSkin.GameModule;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.foodust.damageSkin.BaseModule.DisplayModule;
import org.foodust.damageSkin.BaseModule.ItemModule;
import org.foodust.damageSkin.BaseModule.TaskModule;
import org.foodust.damageSkin.DamageSkin;
import org.foodust.damageSkin.Data.SkinData;
import org.foodust.damageSkin.Data.info.SkinInfo;

import java.util.Random;
import java.util.UUID;

public class DamageSkinModule {
    private final DamageSkin plugin;
    private final DisplayModule displayModule;
    private final ItemModule itemModule;
    private final TaskModule taskModule;

    public DamageSkinModule(DamageSkin plugin) {
        this.plugin = plugin;
        this.displayModule = new DisplayModule();
        this.itemModule = new ItemModule();
        this.taskModule = new TaskModule(plugin);
    }

    private String convertDamageToCustomText(SkinInfo skinInfo, double damage) {
        // 데미지를 정수로 변환하고 각 자릿수를 문자열로 분리
        String damageStr = String.valueOf((int) damage);
        StringBuilder result = new StringBuilder();

        // 각 자릿수별로 처리
        for (char digit : damageStr.toCharArray()) {
            String digitStr = String.valueOf(digit);
            String customChar = skinInfo.getCharacters().getOrDefault(digitStr, digitStr);
            result.append(customChar);
        }
        return result.toString();
    }

    public void DamagedTarget(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) return;
        double damage = event.getDamage();
        UUID uuid = player.getUniqueId();

        // 1. playerSkins에서 SkinInfo 확인
        SkinInfo skinInfo = SkinData.playerSkins.get(uuid);
        if (skinInfo == null) {
            return;
        }

        // 데미지 텍스트 포맷팅
        String damageText = convertDamageToCustomText(skinInfo, damage);

        // 랜덤 위치 생성
        Vector minRandom = skinInfo.getMinRandom();
        Vector maxRandom = skinInfo.getMaxRandom();
        Random random = new Random();
        // min과 max 사이의 랜덤값 계산
        double randomX = minRandom.getX() + random.nextDouble() * (maxRandom.getX() - minRandom.getX());
        double randomY = minRandom.getY() + random.nextDouble() * (maxRandom.getY() - minRandom.getY());
        double randomZ = minRandom.getZ() + random.nextDouble() * (maxRandom.getZ() - minRandom.getZ());

        // 엔티티의 바운딩 박스 기준으로 TextDisplay 생성
        BoundingBox boundingBox = entity.getBoundingBox();
        Vector center = boundingBox.getCenter();
        Location entityLocation = new Location(player.getWorld(), center.getX() + randomX, center.getY() + livingEntity.getEyeHeight() + randomY, center.getZ() + randomZ);

        TextDisplay textDisplay = displayModule.makeTextDisplay(
                entity,
                entityLocation,
                damageText,
                skinInfo.getSize(),
                skinInfo.getBillboard()
        );
        textDisplay.setDefaultBackground(false);
        textDisplay.setShadowed(false);
        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));

        long duration = skinInfo.getDuration();
        double speed = skinInfo.getSpeed();
        Vector moveVector = skinInfo.getLocation().clone();
        // 이동 태스크 생성
        BukkitTask moveTask = taskModule.runBukkitTaskTimer(() -> {
            // TextDisplay의 현재 위치 업데이트
            Location currentDisplayLoc = textDisplay.getLocation().clone();
            currentDisplayLoc.add(
                    moveVector.getX() * speed,
                    moveVector.getY() * speed,
                    moveVector.getZ() * speed
            );

            textDisplay.teleport(currentDisplayLoc);
        }, 0L, 1L);

        // duration 후 제거 태스크
        taskModule.runBukkitTaskLater(() -> {
            taskModule.cancelBukkitTask(moveTask);
            textDisplay.remove();
        }, duration);
    }
}
