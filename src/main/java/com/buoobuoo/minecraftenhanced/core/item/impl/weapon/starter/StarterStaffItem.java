package com.buoobuoo.minecraftenhanced.core.item.impl.weapon.starter;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.ItemRarity;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.item.additional.attributes.impl.attack.BaseMagicItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.additional.requirements.impl.PlayerLevelRequirement;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.Cooldown;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.ItemLevel;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.NotStackable;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.MagicWeapon;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class StarterStaffItem extends AttributedItem implements NotStackable, ItemLevel, MagicWeapon, Cooldown {
    public StarterStaffItem() {
        super("STARTER_STAFF", MatRepo.STARTER_STAFF, "Starter Staff");

        this.setRarity(ItemRarity.COMMON);
        this.addRequirements(new PlayerLevelRequirement(1));

        this.addAttributes(new BaseMagicItemAttribute(3));
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
    public void spawnParticle(Location loc){
        Particle.DustOptions dustR = new Particle.DustOptions(Color.WHITE, 1F);
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 1, 50, dustR);
    }
}
