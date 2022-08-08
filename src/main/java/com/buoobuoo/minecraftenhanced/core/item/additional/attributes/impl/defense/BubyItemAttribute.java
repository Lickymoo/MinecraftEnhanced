package com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.defense;

import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.player.ProfileStatInstance;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BubyItemAttribute extends ItemAttribute {

    public BubyItemAttribute(double minRoll, double maxRoll) {
        super("Bubylicious", "BUBYLICIOUS", minRoll, maxRoll);
        this.setRoundValue(true);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance) {
        return "&r&7Bubylicious";
    }

    @Override
    public void onDamage(DamageInstance damageInstance, ItemAttributeInstance instance) {

    }

    @Override
    public void onCalc(ProfileStatInstance statInstance, ItemAttributeInstance instance) {
        ProfileData profileData = statInstance.getProfileData();
        Player player = Bukkit.getPlayer(profileData.getOwnerID());
        if(!player.getName().contains("Buby")){
            player.getInventory().setHelmet(null);
            player.sendMessage(Util.formatColour("&c&lYOU CANNOT HANDLE BUBY"));
            player.getLocation().getWorld().createExplosion(player.getLocation(), 10, false);
            player.getLocation().getWorld().strikeLightning(player.getLocation());
            profileData.setHealth(0);
        }
    }
}
