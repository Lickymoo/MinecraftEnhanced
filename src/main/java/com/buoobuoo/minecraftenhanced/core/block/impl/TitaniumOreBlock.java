package com.buoobuoo.minecraftenhanced.core.block.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.ToolType;
import com.buoobuoo.minecraftenhanced.core.block.CustomBlock;
import org.bukkit.Instrument;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TitaniumOreBlock extends CustomBlock {
    public TitaniumOreBlock() {
        super(0, Instrument.BANJO, 6, ToolType.PICKAXE, null, Sound.BLOCK_STONE_BREAK);
    }

    @Override
    public int breakSpeed(MinecraftEnhanced plugin, ItemStack itemHeld) {
        return 0;
    }

    @Override
    public void onBreak(MinecraftEnhanced plugin, BlockBreakEvent event) {

    }

    @Override
    public void onInteract(MinecraftEnhanced plugin, PlayerInteractEvent event) {

    }
}
