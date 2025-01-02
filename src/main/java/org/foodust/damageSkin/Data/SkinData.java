package org.foodust.damageSkin.Data;

import org.foodust.damageSkin.Data.info.SkinInfo;

import java.util.HashMap;
import java.util.UUID;

public class SkinData {
    public static HashMap<String, SkinInfo> skins = new HashMap<>();
    public static HashMap<UUID, SkinInfo> playerSkins = new HashMap<>();

    public static void release() {
        skins.clear();
        playerSkins.clear();
    }
}
