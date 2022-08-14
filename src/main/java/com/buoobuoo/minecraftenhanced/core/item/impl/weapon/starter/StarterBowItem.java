package com.buoobuoo.minecraftenhanced.core.item.impl.weapon.starter;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.ItemRarity;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack.BaseRangedItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.requirements.impl.PlayerLevelRequirement;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.Cooldown;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.ItemLevel;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.NotStackable;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.RangedWeapon;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;

public class StarterBowItem extends AttributedItem implements NotStackable, ItemLevel, Cooldown, RangedWeapon {
    public StarterBowItem() {
        super("STARTER_BOW", MatRepo.STARTER_BOW, "Starter Bow");

        this.setRarity(ItemRarity.COMMON);
        this.addRequirements(new PlayerLevelRequirement(1));

        this.addAttributes(new BaseRangedItemAttribute(3));
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

    @Override
    public Class<? extends Projectile> projectileType() {
        return Arrow.class;
    }
}
