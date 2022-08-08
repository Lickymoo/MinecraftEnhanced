package com.buoobuoo.minecraftenhanced.core.item.interfaces;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;

public interface ItemLevel extends Modifier{

    int itemLevel();

    @Override
    default void modifierCreate(MinecraftEnhanced plugin, ItemBuilder ib){
        ib.nbtInt(plugin, "ITEM_LEVEL", itemLevel());
        ib.lore(1, "&r&7Item Level &f" + itemLevel());
    }
}
