package com.buoobuoo.minecraftenhanced.core.item.interfaces;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;

import java.util.UUID;

public interface NotStackable extends Modifier{

    @Override
    default void modifierCreate(MinecraftEnhanced plugin, ItemBuilder ib){
        ib.nbtString(plugin, "RAND_ID", UUID.randomUUID().toString());
    }
}
