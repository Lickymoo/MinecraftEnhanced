package com.buoobuoo.minecraftenhanced.core.item.interfaces;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;

public interface Modifier {
    void modifierCreate(MinecraftEnhanced plugin, ItemBuilder ib);
}
