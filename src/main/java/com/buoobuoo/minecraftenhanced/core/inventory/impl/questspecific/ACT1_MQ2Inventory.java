package com.buoobuoo.minecraftenhanced.core.inventory.impl.questspecific;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.questrelated.ACT1_MQ2PickItemEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.item.CustomItems;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ACT1_MQ2Inventory extends CustomInventory {
    public ACT1_MQ2Inventory(MinecraftEnhanced plugin, Player player) {
        super(plugin, player, "&8Choose a weapon", 27);

        this.addDefaultHandler(event -> {
            ItemStack item = event.getCurrentItem();

            if(item == null)
                return;

            player.getInventory().addItem(item);

            ACT1_MQ2PickItemEvent ev = new ACT1_MQ2PickItemEvent(player);
            Bukkit.getPluginManager().callEvent(ev);
            player.closeInventory();
        });
    }



    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));
        CustomItemManager customItemManager = plugin.getCustomItemManager();

        ProfileData profileData = plugin.getPlayerManager().getProfile(player);

        inv.addItem(customItemManager.getItem(profileData, CustomItems.STARTER_SWORD));
        inv.addItem(customItemManager.getItem(profileData, CustomItems.STARTER_STAFF));
        inv.addItem(customItemManager.getItem(profileData, CustomItems.STARTER_BOW));
        return inv;
    }
}
