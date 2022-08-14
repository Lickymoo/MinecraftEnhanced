package com.buoobuoo.minecraftenhanced.core.item.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.ItemRarity;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.defense.MaximumHealthItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.defense.MaximumManaItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.requirements.impl.PlayerLevelRequirement;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.ItemLevel;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.NotStackable;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.Armour;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import org.bukkit.Material;

public class BubysHelmetItem extends AttributedItem implements NotStackable, ItemLevel, Armour {
    public BubysHelmetItem() {
        super("BUBYS_HELMET", Material.LEATHER_HELMET, "Buby's Helmet");

        this.setRarity(ItemRarity.ULTRA_RARE);
        this.addRequirements(new PlayerLevelRequirement(100));

        this.addAttributes(new MaximumHealthItemAttribute(100, 100));
        this.addAttributes(new MaximumManaItemAttribute(100, 100));
    }

    @Override
    public int itemLevel() {
        return 100;
    }

    @Override
    public void modifierCreate(MinecraftEnhanced plugin, ItemBuilder ib) {
        NotStackable.super.modifierCreate(plugin, ib);
        ItemLevel.super.modifierCreate(plugin, ib);
    }
}
