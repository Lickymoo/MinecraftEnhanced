package com.buoobuoo.minecraftenhanced.core.item.impl.weapon.starter;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.ItemRarity;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack.BasePhysicalItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.requirements.impl.PlayerLevelRequirement;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.Cooldown;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.ItemLevel;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.NotStackable;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.MeleeWeapon;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;

public class StarterAxeItem extends AttributedItem implements NotStackable, ItemLevel, MeleeWeapon, Cooldown {
    public StarterAxeItem() {
        super("STARTER_AXE", MatRepo.STARTER_AXE, "Starter Axe");

        this.setRarity(ItemRarity.COMMON);
        this.addRequirements(new PlayerLevelRequirement(1));

        this.addAttributes(new BasePhysicalItemAttribute(5));
    }

    @Override
    public int cooldownTicks() {
        return 20;
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
