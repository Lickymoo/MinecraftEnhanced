package com.buoobuoo.minecraftenhanced.core.block;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.ToolType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

@Getter
@AllArgsConstructor
public abstract class CustomBlock {
    private int note;
    private Instrument instrument;
    private float hardness;
    private ToolType bestTool;
    private ToolType exclusiveHarvestTool;
    private Sound breakSound;

    public void placeAt(Block block){
        block.setType(Material.NOTE_BLOCK);
        NoteBlock nb = (NoteBlock) block.getBlockData();
        nb.setNote(new Note(note));
        nb.setInstrument(instrument);
        block.setBlockData(nb);
    }

    public void placeAt(Block block, Plugin plugin, long delay){
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                new Runnable()  {
                    public void run() {
                        placeAt(block);
                    }
        }, delay);
    }

    public void placeAt(Location location){
        placeAt(location.getBlock());
    }

    public abstract int breakSpeed(MinecraftEnhanced plugin, ItemStack itemHeld);
    public abstract void onBreak(MinecraftEnhanced plugin, BlockBreakEvent event);
    public abstract void onInteract(MinecraftEnhanced plugin, PlayerInteractEvent event);
}
