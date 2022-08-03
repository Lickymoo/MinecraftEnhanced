package com.buoobuoo.minecraftenhanced.core.item.attributes.impl;

import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import com.buoobuoo.minecraftenhanced.core.item.attributes.ItemAttributeInstance;
import com.buoobuoo.minecraftenhanced.core.item.attributes.VariableItemAttribute;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;

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
    public void calculate(ProfileData profileData) {

    }

    @Override
    public void onDamage(DamageInstance damageInstance, ItemAttributeInstance instance) {
        damageInstance.setDamageDealt(instance.getValue());
    }
}
