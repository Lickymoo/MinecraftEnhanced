package com.buoobuoo.minecraftenhanced.core.inventory;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class CustomInventoryRegistry {

    private final static List<CustomInventory> customInventoryList = new ArrayList<>();
    private final MinecraftEnhanced plugin;

    public CustomInventoryRegistry(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    public void registerInventory(CustomInventory... inventories){
        for(CustomInventory inventory : inventories){
            customInventoryList.add(inventory);
            plugin.registerEvents(inventory);
        }
    }

    public void unregisterInventory(CustomInventory... inventories){
        for(CustomInventory inventory : inventories){
            customInventoryList.remove(inventory);
            HandlerList.unregisterAll(inventory);
        }
    }

    public CustomInventory getHandler(Inventory inventory){
        for(CustomInventory inv : customInventoryList){
            if(inv == null || inventory == null)
                continue;

            if(inventory.getHolder() == null)
                continue;

            if(inv == inventory.getHolder())
                return inv;
        }
        return null;
    }

}
