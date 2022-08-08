package com.buoobuoo.minecraftenhanced.core.event.listener;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.impl.playermenu.PlayerMenuMainInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public class PlayerManagerItemListener implements Listener {

    private final MinecraftEnhanced plugin;

    public PlayerManagerItemListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(event.getPlayer().getInventory().getHeldItemSlot() != 8)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void interact(PlayerInteractEvent event){
        if(event.getPlayer().getInventory().getHeldItemSlot() != 8)
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        Inventory menu = new PlayerMenuMainInventory(plugin, player).getInventory();
        player.openInventory(menu);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void click(InventoryClickEvent event){

        Inventory inv = event.getClickedInventory();
        if(!(inv instanceof PlayerInventory))
            return;

        int slot = event.getSlot();
        if(slot != 8)
            return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        Inventory menu = new PlayerMenuMainInventory(plugin, player).getInventory();
        player.openInventory(menu);
    }
}
