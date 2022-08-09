package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.MoistureChangeEvent;

public class MoistureChangeEventListener implements Listener {

    private final MinecraftEnhanced plugin;

    public MoistureChangeEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(MoistureChangeEvent event){
        event.setCancelled(true);
    }
}
