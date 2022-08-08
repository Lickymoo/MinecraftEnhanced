package com.buoobuoo.minecraftenhanced.core.inventory.impl.playermenu;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerMenuMainInventory extends CustomInventory {
    public PlayerMenuMainInventory(MinecraftEnhanced plugin, Player player) {
        super(plugin, player, "Player Menu", 27);

        this.addDefaultHandler( event -> {
            Inventory inv = new AbilityInventory(plugin, player).getInventory();
            player.openInventory(inv);
        });
    }
}
