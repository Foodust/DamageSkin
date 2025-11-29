package org.foodust.damageSkin.module;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.foodust.damageSkin.DamageSkin;

import java.io.File;
import java.io.IOException;

public abstract class BaseModule {
    public final DamageSkin plugin;
    public String fileName;
    private File file;
    protected FileConfiguration config;

    public BaseModule(DamageSkin plugin) {
        this.plugin = plugin;
    }

    public void reloadConfig() {
        if (fileName == null) return;
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        if (file == null || config == null) return;
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save config: " + fileName);
        }
    }
}
