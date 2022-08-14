package com.buoobuoo.minecraftenhanced.core.item.impl.weapon;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.ItemRarity;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack.BasePhysicalItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack.CriticalMultiplierItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack.CriticalStrikeItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.ItemLevel;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.NotStackable;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.RangedWeapon;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Projectile;

public class TestBowItem extends AttributedItem implements NotStackable, ItemLevel, RangedWeapon {
    public TestBowItem() {
        super("TEST_BOW", Material.BOW, "Test Bow");

        this.setRarity(ItemRarity.ULTRA_RARE);
        this.addAttributes(new BasePhysicalItemAttribute(100000));
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

    @Override
    public Class<? extends Projectile> projectileType() {
        return Egg.class;
    }
}
