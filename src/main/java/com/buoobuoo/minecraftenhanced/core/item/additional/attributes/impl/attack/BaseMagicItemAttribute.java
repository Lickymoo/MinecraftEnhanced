package com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack;

import com.buoobuoo.minecraftenhanced.core.entity.EntityStatInstance;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.VariableItemAttribute;

import java.text.DecimalFormat;

public class BaseMagicItemAttribute extends VariableItemAttribute {
    public BaseMagicItemAttribute(double value) {
        super("Magic Damage", "BASE_MAGIC_DAMAGE", value);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance){
        return "&r&f" + new DecimalFormat("#").format(value) + " &7Base Magic Damage";
    }

    @Override
    public void onCalc(EntityStatInstance statInstance, ItemAttributeInstance instance) {
        statInstance.increaseDamageDealt(instance.getValue());
    }
}
