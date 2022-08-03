package com.buoobuoo.minecraftenhanced.core.item.attributes.impl;

import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import com.buoobuoo.minecraftenhanced.core.item.attributes.ItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.attributes.ItemAttributeInstance;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;

public class AttackSpeedItemAttribute extends ItemAttribute {
    public AttackSpeedItemAttribute() {
        super("Attack Speed", "ATTACK_SPEED", 4, 10);
        this.setRoundValue(true);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance) {
        return "&r&f" + instance.getValueFormat() + " &7Increased Attack Speed";
    }

    @Override
    public void calculate(ProfileData profileData) {

    }

    @Override
    public void onDamage(DamageInstance damageInstance, ItemAttributeInstance instance) {

    }
}
