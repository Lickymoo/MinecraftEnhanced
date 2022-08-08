package com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.defense;

import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.minecraftenhanced.core.player.ProfileStatInstance;

public class MaximumHealthItemAttribute extends ItemAttribute {

    public MaximumHealthItemAttribute(double minRoll, double maxRoll) {
        super("Maximum Health", "MAXIMUM_HEALTH", minRoll, maxRoll);
        this.setRoundValue(true);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance) {
        return "&r&f" + instance.getValueFormat() + " &7Increased Maximum Health";
    }

    @Override
    public void onDamage(DamageInstance damageInstance, ItemAttributeInstance instance) {

    }

    @Override
    public void onCalc(ProfileStatInstance statInstance, ItemAttributeInstance instance) {
        statInstance.increaseMaxHealth(instance.getValue());
    }
}
