package com.buoobuoo.minecraftenhanced.core.item.interfaces;

import com.buoobuoo.minecraftenhanced.core.block.CustomBlocks;
import org.bukkit.event.Listener;

public interface BlockItem extends Listener {
    public CustomBlocks blockPlaced();

}
