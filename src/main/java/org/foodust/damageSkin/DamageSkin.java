package org.foodust.damageSkin;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.foodust.damageSkin.command.CommandManager;
import org.foodust.damageSkin.module.SkinModule;

@Getter
public final class DamageSkin extends JavaPlugin {

    private SkinModule skinModule;

    @Override
    public void onEnable() {
        this.skinModule = new SkinModule(this);
        new CommandManager(this);
    }

    @Override
    public void onDisable() {
        if (skinModule != null) {
            skinModule.release();
        }
    }
}
