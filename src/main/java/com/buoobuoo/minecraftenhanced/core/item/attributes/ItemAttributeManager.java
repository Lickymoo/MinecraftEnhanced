package com.buoobuoo.minecraftenhanced.core.item.attributes;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import lombok.Getter;

@Getter
public class ItemAttributeManager {
    private final MinecraftEnhanced plugin;
    private final ItemAttributeRegistry registry;

    public ItemAttributeManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
        this.registry = new ItemAttributeRegistry(plugin);
    }
}
