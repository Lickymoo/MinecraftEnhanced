package com.buoobuoo.minecraftenhanced.core.item.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.ItemRarity;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack.BasePhysicalItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack.CriticalMultiplierItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack.CriticalStrikeItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.requirements.impl.PlayerLevelRequirement;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.ItemLevel;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.NotStackable;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.MeleeWeapon;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;

public class IceSwordItem extends AttributedItem implements NotStackable, ItemLevel, MeleeWeapon {
    public IceSwordItem() {
        super("ICE_SWORD", MatRepo.ICE_SWORD, "Ice Sword");

        this.setRarity(ItemRarity.ULTRA_RARE);
        this.addRequirements(new PlayerLevelRequirement(10));

        this.addAttributes(new BasePhysicalItemAttribute(10));
        this.addAttributes(new CriticalStrikeItemAttribute(50, 75));
        this.addAttributes(new CriticalMultiplierItemAttribute(100, 200));
    }

    @Override
    public int itemLevel() {
        return 1;
    }

    @Override
    public void modifierCreate(MinecraftEnhanced plugin, ItemBuilder ib) {
        NotStackable.super.modifierCreate(plugin, ib);
        ItemLevel.super.modifierCreate(plugin, ib);
    }
}
