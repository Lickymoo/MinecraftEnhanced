package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealthEventListener implements Listener {

    private final MinecraftEnhanced plugin;

    public EntityRegainHealthEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(EntityRegainHealthEvent event){
        event.setCancelled(true);
    }
}
