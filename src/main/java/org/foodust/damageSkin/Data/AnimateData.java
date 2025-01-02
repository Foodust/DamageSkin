package org.foodust.damageSkin.Data;

import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class AnimateData {
    public static List<Entity> ENTITIES = new ArrayList<>();

    public static void release() {
        ENTITIES.forEach(entity -> {
            if (entity != null) {
                entity.remove();
            }
        });
    }
}
