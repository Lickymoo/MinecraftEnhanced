package com.buoobuoo.minecraftenhanced.core.damage;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.abilitycasttype.PlayerCritEvent;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.MagicWeapon;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.MeleeWeapon;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.RangedWeapon;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.player.tempmodifier.TemporaryDamageModifier;
import com.buoobuoo.minecraftenhanced.core.util.Hologram;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DamageManager {
    private final MinecraftEnhanced plugin;

    public DamageManager(MinecraftEnhanced plugin) {
        this.plugin = plugin;
    }

    public DamageInstance calculateDamage(Player player, Entity ent, DamageType type, double effectiveness){

        PlayerInventory inv = player.getInventory();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        ItemStack hand = inv.getItemInMainHand();

        DamageInstance damageInstance = new DamageInstance(profileData, ent, hand);
        CustomItem handler = plugin.getCustomItemManager().getRegistry().getHandler(hand);
        if(!(handler instanceof AttributedItem))
            return damageInstance;

        switch (type){
            case RANGED:
                if(!(handler instanceof RangedWeapon))
                    return damageInstance;
                break;
            case MELEE:
                if(!(handler instanceof MeleeWeapon))
                    return damageInstance;
                break;
            case MAGIC:
                if(!(handler instanceof MagicWeapon))
                    return damageInstance;
                break;
            case SPELL:
                break;
        }

        AttributedItem aHandler = (AttributedItem)handler;

        ((AttributedItem) handler).onDamage(plugin, damageInstance);

        for(TemporaryDamageModifier damageModifier : profileData.getTemporaryDamageModifiers()){
            damageModifier.getInstanceConsumer().accept(damageInstance);
            profileData.getTemporaryDamageModifiers().remove(damageModifier);
        }

        double damage = damageInstance.getDamageDealt() * effectiveness;

        //calc crit
        double critMulti = damageInstance.getCritStrikeMulti();
        double critChance = damageInstance.getCritStrikeChance();

        int chance = Util.randomInt(0, 100);
        if(chance <= critChance) {
            double multi = critMulti/100;

            damageInstance.setDamageDealt(multi * damage);
            damageInstance.setDamageIndicatorPrefix("&c&l");

            Bukkit.getPluginManager().callEvent(new PlayerCritEvent(player, damageInstance.getEnt()));
        }

        //Spawn damage indicator
        double xOffset = Util.randomDouble(-1, 1);
        double zOffset = Util.randomDouble(-1, 1);
        Location loc = ent.getLocation().clone().add(xOffset, 0, zOffset);

        String damageText = Util.formatDouble(damageInstance.getDamageDealt());
        Hologram.spawnHologram(plugin, loc, false, 20, UnicodeSpaceUtil.getNeg(8 * damageText.length()) + damageInstance.getDamageIndicatorPrefix() + damageText);

        return damageInstance;
    }
}
