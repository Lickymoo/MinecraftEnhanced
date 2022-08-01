package com.buoobuoo.minecraftenhanced.core.item.impl;

import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TitaniumIngotItem extends CustomItem {
    public TitaniumIngotItem() {
        super("TITANIUM_INGOT", Material.PAPER, 1003, "Titanium Ingot");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;


    }
}
