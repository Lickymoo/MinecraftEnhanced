package com.buoobuoo.minecraftenhanced.core.item.impl;

import com.buoobuoo.minecraftenhanced.core.item.ItemRarity;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.item.attributes.impl.AttackSpeedItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.attributes.impl.BasePhysicalItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.NotStackable;

public class IceSwordItem extends AttributedItem implements NotStackable {
    public IceSwordItem() {
        super("ICE_SWORD", MatRepo.ICE_SWORD, "Ice Sword");

        this.setRarity(ItemRarity.ULTRA_RARE);
        this.addAttributes(new BasePhysicalItemAttribute(10));
        this.addAttributes(new AttackSpeedItemAttribute());
    }
}
