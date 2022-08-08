package com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack;

import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.VariableItemAttribute;
import com.buoobuoo.minecraftenhanced.core.player.ProfileStatInstance;

import java.text.DecimalFormat;

public class BasePhysicalItemAttribute extends VariableItemAttribute {
    public BasePhysicalItemAttribute(double value) {
        super("Physical Damage", "BASE_PHYSICAL_DAMAGE", value);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance){
        return "&r&f" + new DecimalFormat("#").format(value) + " &7Base Physical Damage";
    }

    @Override
    public void onDamage(DamageInstance damageInstance, ItemAttributeInstance instance) {
        damageInstance.increaseDamageDealt(instance.getValue());
    }

    @Override
    public void onCalc(ProfileStatInstance statInstance, ItemAttributeInstance instance) {

    }
}
