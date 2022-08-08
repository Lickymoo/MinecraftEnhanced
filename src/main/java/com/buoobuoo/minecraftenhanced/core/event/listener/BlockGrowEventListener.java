package com.buoobuoo.minecraftenhanced.core.event.listener;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class BlockGrowEventListener implements Listener {

    private final MinecraftEnhanced plugin;

    public BlockGrowEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(event.getPlayer().getInventory().getHeldItemSlot() != 8)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void grow(BlockFormEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void spread(BlockSpreadEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void grow(BlockGrowEvent event){
        event.setCancelled(true);
    }
}
