package com.buoobuoo.minecraftenhanced.core.item.impl;

import com.buoobuoo.minecraftenhanced.core.block.CustomBlocks;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.BlockItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class MysteryBoxItem extends CustomItem implements BlockItem {
    public MysteryBoxItem() {
        super("MYSTERY_BOX", Material.PAPER, 1003, "Mystery Box");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;


    }

    @Override
    public CustomBlocks blockPlaced() {
        return CustomBlocks.MYSTERY_BOX;
    }
}
