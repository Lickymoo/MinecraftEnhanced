package com.buoobuoo.minecraftenhanced.core.inventory;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Consumer;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class CustomInventory implements InventoryHolder, Listener {
    protected final MinecraftEnhanced plugin;
    protected final Player player;
    protected Map<Integer, Consumer<InventoryClickEvent>> slotMap = new HashMap<>();

    protected String title;
    protected int size;
    protected boolean cancelBottomClick = true;

    public CustomInventory(MinecraftEnhanced plugin, Player player, String title, int size){
        this.plugin = plugin;
        this.title = title;
        this.size = size;
        this.player = player;

        plugin.getCustomInventoryManager().getRegistry().registerInventory(this);
    }

    public void addHandler(Consumer<InventoryClickEvent> event, int... slots){
        for(int i : slots){
            slotMap.put(i, event);
        }
    }

    public void addDefaultHandler(Consumer<InventoryClickEvent> event){
        for(int i = 0; i < size; i++){
            slotMap.put(i, event);
        }
    }

    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(this, size, title);
    }

    public boolean isApplicable(Inventory inventory){
        return inventory.getHolder() == this;
    }

    public void onClose(InventoryCloseEvent event){
    }

    public void onBottomClick(InventoryClickEvent event){

    }
}






































