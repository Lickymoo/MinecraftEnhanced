package com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack;

import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.minecraftenhanced.core.player.ProfileStatInstance;

public class CriticalStrikeItemAttribute extends ItemAttribute {
    public CriticalStrikeItemAttribute(double minRoll, double maxRoll) {
        super("Critical Strike Chance", "CRITICAL_STRIKE_CHANCE", minRoll, maxRoll);
        this.setRoundValue(true);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance) {
        return "&r&f" + instance.getValueFormat() + "% &7Critical strike chance";
    }

    @Override
    public void onDamage(DamageInstance damageInstance, ItemAttributeInstance instance) {
        damageInstance.increaseCritStrikeChance(instance.getValue());
    }

    @Override
    public void onCalc(ProfileStatInstance statInstance, ItemAttributeInstance instance) {

    }
}
