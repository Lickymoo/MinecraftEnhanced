package com.buoobuoo.minecraftenhanced.core.block;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;

import java.util.ArrayList;
import java.util.List;

public class CustomBlockRegistry {
    private final static List<CustomBlock> customBlockList = new ArrayList<>();
    private final MinecraftEnhanced plugin;

    public CustomBlockRegistry(MinecraftEnhanced plugin){
        this.plugin = plugin;
        registerBlock(CustomBlocks.getHandlers());
    }

    public void registerBlock(CustomBlock... blocks){
        for(CustomBlock block : blocks){
            customBlockList.add(block);
        }
    }

    public CustomBlock getHandler(Block block){
        if(block.getType() != Material.NOTE_BLOCK)
            return null;

        NoteBlock nbData = (NoteBlock)block.getBlockData();
        int note = Byte.toUnsignedInt(nbData.getNote().getId());
        Instrument instrument = nbData.getInstrument();

        for(CustomBlock customBlock : customBlockList){
            if(customBlock.getNote() == note && customBlock.getInstrument().name().equals(instrument.name())) {
                return customBlock;
            }
        }
        return null;
    }

    public boolean isCustomBlock(Block block){
        return getHandler(block) != null;
    }
}




























































