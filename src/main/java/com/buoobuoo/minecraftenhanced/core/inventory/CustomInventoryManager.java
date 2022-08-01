package com.buoobuoo.minecraftenhanced.core.inventory;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

@Getter
public class CustomInventoryManager implements Listener {

    private final MinecraftEnhanced plugin;
    private final CustomInventoryRegistry registry;

    public CustomInventoryManager(MinecraftEnhanced plugin) {
        this.plugin = plugin;
        this.registry = new CustomInventoryRegistry(plugin);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Inventory inv = event.getInventory();
        CustomInventory handler = registry.getHandler(inv);
        registry.unregisterInventory(handler);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory inv = event.getClickedInventory();
        if(inv != event.getWhoClicked().getOpenInventory().getTopInventory())
            return;

        CustomInventory handler = registry.getHandler(inv);
        if(handler == null)
            return;

        event.setCancelled(true);


        int slot = event.getSlot();
        if(handler.slotMap.get(slot) != null)
            handler.slotMap.get(slot).accept(event);
    }

}
