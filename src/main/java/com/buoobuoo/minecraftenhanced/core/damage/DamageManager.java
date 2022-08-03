package com.buoobuoo.minecraftenhanced.core.damage;

import co.aikar.commands.PaperCommandManager;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.util.Hologram;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DamageManager {
    private final MinecraftEnhanced plugin;

    public DamageManager(MinecraftEnhanced plugin) {
        this.plugin = plugin;
    }

    public DamageInstance calculateDamage(Player player, Entity ent){

        PlayerInventory inv = player.getInventory();
        ItemStack hand = inv.getItemInMainHand();

        DamageInstance damageInstance = new DamageInstance(ent, hand);
        CustomItem handler = plugin.getCustomItemManager().getRegistry().getHandler(hand);
        if(!(handler instanceof AttributedItem))
            return damageInstance;

        AttributedItem aHandler = (AttributedItem)handler;

        ((AttributedItem) handler).onDamage(plugin, damageInstance);

        //Spawn damage indicator
        double xOffset = Util.randomDouble(-1, 1);
        double zOffset = Util.randomDouble(-1, 1);
        Location loc = ent.getLocation().clone().add(xOffset, 0, zOffset);
        Hologram.spawnHologram(plugin, loc, false, 20, damageInstance.getDamageIndicatorPrefix() + damageInstance.getDamageDealt());

        return damageInstance;
    }
}
