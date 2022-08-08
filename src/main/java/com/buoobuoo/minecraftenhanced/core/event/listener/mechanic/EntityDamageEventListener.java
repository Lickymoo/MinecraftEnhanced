package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageEventListener implements Listener {

    private final MinecraftEnhanced plugin;

    public EntityDamageEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void listen(EntityDamageEvent event){
        EntityDamageEvent.DamageCause cause = event.getCause();
        if(cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            return;
        if (cause == EntityDamageEvent.DamageCause.PROJECTILE)
            return;
        if (cause == EntityDamageEvent.DamageCause.CUSTOM)
            return;

        event.setCancelled(true);

    }
}

































