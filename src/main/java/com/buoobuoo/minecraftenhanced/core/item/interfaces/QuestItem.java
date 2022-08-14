package com.buoobuoo.minecraftenhanced.core.item.interfaces;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;

public interface QuestItem extends Modifier{

    @Override
    default void modifierCreate(MinecraftEnhanced plugin, ItemBuilder ib){
        ib.nbtInteger(plugin, "QUEST_ITEM", 1);
    }
}
