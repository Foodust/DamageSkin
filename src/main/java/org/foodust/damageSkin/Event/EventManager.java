package org.foodust.damageSkin.Event;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.foodust.damageSkin.DamageSkin;

public class EventManager {
    public EventManager(Server server, Plugin plugin) {
        server.getPluginManager().registerEvents(new DamageEvent((DamageSkin) plugin), plugin);
    }
}
