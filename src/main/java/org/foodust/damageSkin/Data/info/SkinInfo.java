package org.foodust.damageSkin.Data.info;

import lombok.*;
import org.bukkit.entity.Display;
import org.bukkit.util.Vector;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkinInfo {
    @Builder.Default
    private Display.Billboard billboard = Display.Billboard.FIXED;

    @Builder.Default
    private long duration = 0;

    @Builder.Default
    private double speed = 0;

    @Builder.Default
    private Vector location = new Vector(0, 0, 0);

    @Builder.Default
    private Vector size = new Vector(0, 0, 0);

    @Builder.Default
    private HashMap<String, String> characters = new HashMap<>();
}
