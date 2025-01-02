package org.foodust.damageSkin.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.foodust.damageSkin.DamageSkin;
import org.foodust.damageSkin.GameModule.DamageSkinModule;

public class DamageEvent implements Listener {
    private final DamageSkin plugin;
    private final DamageSkinModule damageSkinModule;

    public DamageEvent(DamageSkin plugin) {
        this.plugin = plugin;
        this.damageSkinModule = new DamageSkinModule(plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        damageSkinModule.DamagedTarget(event);
    }
}
