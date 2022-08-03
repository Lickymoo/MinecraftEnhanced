package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealthEventListener implements Listener {
    @EventHandler
    public void listen(EntityRegainHealthEvent event){
        event.setCancelled(true);
    }
}
