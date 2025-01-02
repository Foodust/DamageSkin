package org.foodust.damageSkin;

import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.foodust.damageSkin.Command.CommandManager;
import org.foodust.damageSkin.Data.TaskData;
import org.foodust.damageSkin.Event.EventManager;

import java.util.logging.Logger;

@Getter
public final class DamageSkin extends JavaPlugin {

    private BukkitAudiences adventure;
    private Plugin plugin;
    private Logger log;

    public @NonNull BukkitAudiences getAdventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.log = Bukkit.getLogger();
        this.adventure = BukkitAudiences.create(this);
        this.plugin = this;
        CommandManager commandManager = new CommandManager(this);
        EventManager eventManager = new EventManager(this.getServer(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        TaskData.release();
    }
}
