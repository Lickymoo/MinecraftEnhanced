package com.buoobuoo.minecraftenhanced.core.block.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.ToolType;
import com.buoobuoo.minecraftenhanced.core.block.CustomBlock;
import com.buoobuoo.minecraftenhanced.core.entity.impl.util.EmptyEntity;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicFrame;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicSequence;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MysteryBoxBlock extends CustomBlock {

    public MysteryBoxBlock() {
        super(1, Instrument.BANJO, 6, ToolType.PICKAXE, null, Sound.BLOCK_STONE_BREAK);
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
        Player player = event.getPlayer();
        Location startLoc = event.getClickedBlock().getLocation().add(.5, 0, -5);
        Location blockLoc = event.getClickedBlock().getLocation();


        EmptyEntity stand = plugin.getSpectatorManager().viewLoc(player, startLoc);

        CinematicFrame[] move1 = new CinematicFrame[] {
                new CinematicFrame(10, z -> z.playSound(blockLoc, Sound.BLOCK_ANVIL_LAND, 1, 1)),
                new CinematicFrame(10, z -> z.playSound(blockLoc, Sound.BLOCK_ANVIL_LAND, 1, 1)),
                new CinematicFrame(10, z -> z.playSound(blockLoc, Sound.BLOCK_ANVIL_LAND, 1, 1)),
                new CinematicFrame(10, z -> z.playSound(blockLoc, Sound.BLOCK_ANVIL_LAND, 1, 1)),
                new CinematicFrame(10, z -> z.playSound(blockLoc, Sound.BLOCK_ANVIL_LAND, 1, 1)),
                new CinematicFrame(10, z -> z.playSound(blockLoc, Sound.BLOCK_ANVIL_LAND, 1, 1)),
                new CinematicFrame(10, z -> z.playSound(blockLoc, Sound.BLOCK_ANVIL_LAND, 1, 1)),
                new CinematicFrame(10, z -> z.playSound(blockLoc, Sound.ITEM_CROSSBOW_HIT, 1, 1)),
                new CinematicFrame(1, z -> blockLoc.getWorld().dropItem(blockLoc.add(.5, 1, .5), new ItemStack(Material.DIRT)).setVelocity(new Vector(0,.1f,0)) ) ,
                new CinematicFrame(50, z -> {}) ,
        };
        new CinematicSequence(plugin, true, CinematicSequence.mergeArrays(move1)).execute(player);
    }
}
