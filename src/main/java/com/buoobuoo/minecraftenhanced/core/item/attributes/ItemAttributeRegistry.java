package com.buoobuoo.minecraftenhanced.core.item.attributes;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.CustomItems;

import java.util.HashMap;
import java.util.Map;

public class ItemAttributeRegistry {
    private final static Map<String, ItemAttribute> attributeList = new HashMap<>();
    private final MinecraftEnhanced plugin;

    public ItemAttributeRegistry(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    public void registerAttribute(ItemAttribute... attributes){
        for(ItemAttribute attribute : attributes){
            attributeList.put(attribute.getId(), attribute);
            plugin.registerEvents(attribute);
        }
    }

}
