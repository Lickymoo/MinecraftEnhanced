package com.buoobuoo.minecraftenhanced.core.entity.impl;

import com.buoobuoo.minecraftenhanced.core.entity.interf.ModelEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

public class RatEntity extends Silverfish implements ModelEntity {
    public RatEntity(Location loc) {
        super(EntityType.SILVERFISH, ((CraftWorld) loc.getWorld()).getHandle());
    }

    @Override
    public String entityID() {
        return "RAT";
    }

    @Override
    public String entityName() {
        return "Rat";
    }

    @Override
    public double maxHealth() {
        return 10;
    }

    @Override
    public double damage() {
        return 0;
    }

    @Override
    public double tagOffset() {
        return -1.5;
    }

    @Override
    public int entityLevel() {
        return 0;
    }

    @Override
    public boolean showHealth() {
        return true;
    }

    @Override
    public String overrideTag() {
        return null;
    }

    @Override
    public String modelName() {
        return "rat";
    }
}

