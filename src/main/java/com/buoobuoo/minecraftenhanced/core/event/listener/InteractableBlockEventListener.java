package com.buoobuoo.minecraftenhanced.core.event.listener;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.google.common.collect.ImmutableList;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class InteractableBlockEventListener implements Listener {

    public static ImmutableList<Material> restrictedContainers = new ImmutableList.Builder<Material>().add(
            Material.CHEST,
            Material.TRAPPED_CHEST,
            Material.HOPPER,
            Material.FURNACE,
            Material.CRAFTING_TABLE,
            Material.ENCHANTING_TABLE,
            Material.ENDER_CHEST,
            Material.BEACON,
            Material.SHULKER_BOX,
            Material.BREWING_STAND,
            Material.LOOM,
            Material.BARREL,
            Material.DISPENSER,
            Material.DROPPER,
            Material.BLAST_FURNACE,
            Material.SMOKER,
            Material.CARTOGRAPHY_TABLE,
            Material.FLETCHING_TABLE,
            Material.GRINDSTONE,
            Material.SMITHING_TABLE,
            Material.STONECUTTER
    ).build();

    private final MinecraftEnhanced plugin;

    public InteractableBlockEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Material mat = event.getClickedBlock().getType();
        if(restrictedContainers.contains(mat))
            event.setCancelled(true);

    }
}
