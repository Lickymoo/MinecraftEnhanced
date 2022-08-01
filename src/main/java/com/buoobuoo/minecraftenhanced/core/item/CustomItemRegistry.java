package com.buoobuoo.minecraftenhanced.core.item;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class CustomItemRegistry {
    private final static Map<String, CustomItem> customItemList = new HashMap<>();
    private final MinecraftEnhanced plugin;

    public CustomItemRegistry(MinecraftEnhanced plugin){
        this.plugin = plugin;
        registerItem(CustomItems.getHandlers());
    }

    public void registerItem(CustomItem... items){
        for(CustomItem item : items){
            customItemList.put(item.getId(), item);
            item.addManager(plugin.getCustomItemManager());
            plugin.registerEvents(item);
        }
    }

    public CustomItem getHandler(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "ITEM_ID");
            String id = pdc.get(key, PersistentDataType.STRING);

            return customItemList.getOrDefault(id, null);
        }
        return null;
    }

    public boolean isCustomItem(ItemStack item){
        return getHandler(item) != null;
    }

    public boolean isApplicable(CustomItem handler, ItemStack item){
        return handler == getHandler(item);
    }
}
