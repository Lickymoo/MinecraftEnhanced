package com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.defense;

import com.buoobuoo.minecraftenhanced.core.entity.EntityStatInstance;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttributeInstance;

public class MaximumManaItemAttribute extends ItemAttribute {

    public MaximumManaItemAttribute(double minRoll, double maxRoll) {
        super("Maximum Mana", "MAXIMUM_MANA", minRoll, maxRoll);
        this.setRoundValue(true);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance) {
        return "&r&f" + instance.getValueFormat() + " &7Increased Maximum Mana";
    }

    @Override
    public void onCalc(EntityStatInstance statInstance, ItemAttributeInstance instance) {
        statInstance.increaseMaxMana(instance.getValue());
    }
}
