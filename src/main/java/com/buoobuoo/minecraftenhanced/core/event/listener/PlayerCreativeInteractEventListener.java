package com.buoobuoo.minecraftenhanced.core.event.listener;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.Weapon;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerCreativeInteractEventListener implements Listener {

    private final MinecraftEnhanced plugin;

    public PlayerCreativeInteractEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(BlockBreakEvent event){
        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();
        CustomItem handler = plugin.getCustomItemManager().getRegistry().getHandler(item);

        if(!(handler instanceof Weapon))
            return;

        event.setCancelled(true);


    }
}
