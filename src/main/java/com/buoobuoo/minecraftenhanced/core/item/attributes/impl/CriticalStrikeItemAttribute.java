package com.buoobuoo.minecraftenhanced.core.item.attributes.impl;

import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import com.buoobuoo.minecraftenhanced.core.item.attributes.ItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.attributes.ItemAttributeInstance;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.Util;

public class CriticalStrikeItemAttribute extends ItemAttribute {
    public CriticalStrikeItemAttribute() {
        super("Critical Strike", "CRITICAL_STRIKE", 4, 10);
        this.setRoundValue(true);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance) {
        return "&r&f" + instance.getValueFormat() + "% &7Chance to deal a critical strike";
    }

    @Override
    public void calculate(ProfileData profileData) {

    }

    @Override
    public void onDamage(DamageInstance damageInstance, ItemAttributeInstance instance) {
        double val = damageInstance.getDamageDealt();
        int chance = Util.randomInt(0, 100);
        System.out.println(chance);
        if(chance <= instance.getValue()) {
            val *= 2;
            damageInstance.setDamageIndicatorPrefix("&c");
        }
        damageInstance.setDamageDealt(val);
    }
}
