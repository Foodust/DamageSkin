package org.foodust.damageSkin.GameModule;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.foodust.damageSkin.BaseModule.DisplayModule;
import org.foodust.damageSkin.BaseModule.ItemModule;
import org.foodust.damageSkin.BaseModule.TaskModule;
import org.foodust.damageSkin.DamageSkin;
import org.foodust.damageSkin.Data.SkinData;
import org.foodust.damageSkin.Data.info.SkinInfo;

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
        double damage = event.getDamage();
        Location entityLocation = entity.getLocation();
        UUID uuid = player.getUniqueId();

        // 1. playerSkins에서 SkinInfo 확인
        SkinInfo skinInfo = SkinData.playerSkins.get(uuid);
        if (skinInfo == null) {
            return;
        }

        // 데미지 텍스트 포맷팅 (characters에서 가져오거나 기본값 사용)
        String damageText = convertDamageToCustomText(skinInfo, damage);

        // 2. TextDisplay 생성
        Location displayLocation = entityLocation.clone().add(
                skinInfo.getLocation().getX(),
                skinInfo.getLocation().getY(),
                skinInfo.getLocation().getZ()
        );

        TextDisplay textDisplay = displayModule.makeTextDisplay(
                entity,
                displayLocation,
                damageText,
                skinInfo.getSize(),
                skinInfo.getBillboard()
        );
        textDisplay.setDefaultBackground(false);
        textDisplay.setShadowed(false);

        // 3 & 4. 애니메이션 처리 (duration 동안 location만큼 상승)
        long duration = skinInfo.getDuration();
        double speed = skinInfo.getSpeed();
        Vector moveVector = skinInfo.getLocation().clone();
        // 이동 태스크 생성
        BukkitTask moveTask = taskModule.runBukkitTaskTimer(() -> {
            Location currentLoc = textDisplay.getLocation();
            currentLoc.add(
                    moveVector.getX() * speed,
                    moveVector.getY() * speed,
                    moveVector.getZ() * speed
            );
            textDisplay.teleport(currentLoc);
        }, 0L, 1L); // speed초마다 실행
        // duration 후 제거 태스크

        taskModule.runBukkitTaskLater(() -> {
            taskModule.cancelBukkitTask(moveTask);
            textDisplay.remove();
        }, duration);
    }
}
