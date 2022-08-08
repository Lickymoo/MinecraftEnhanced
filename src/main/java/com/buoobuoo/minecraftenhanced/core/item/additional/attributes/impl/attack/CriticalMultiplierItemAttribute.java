package com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack;

import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.minecraftenhanced.core.player.ProfileStatInstance;

public class CriticalMultiplierItemAttribute extends ItemAttribute {
    public CriticalMultiplierItemAttribute(double minRoll, double maxRoll) {
        super("Critical Strike Multi", "CRITICAL_STRIKE_MULTI", minRoll, maxRoll);
        this.setRoundValue(true);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance) {
        return "&r&f" + instance.getValueFormat() + "% &7Increased critical strike multiplier";
    }

    @Override
    public void onDamage(DamageInstance damageInstance, ItemAttributeInstance instance) {
        damageInstance.increaseCritStrikeMulti(instance.getValue());
    }

    @Override
    public void onCalc(ProfileStatInstance statInstance, ItemAttributeInstance instance) {

    }
}
