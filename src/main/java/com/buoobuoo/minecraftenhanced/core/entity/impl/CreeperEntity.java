package com.buoobuoo.minecraftenhanced.core.entity.impl;

import com.buoobuoo.minecraftenhanced.core.entity.interf.ModelEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

public class CreeperEntity extends Creeper implements ModelEntity {
    public CreeperEntity(Location loc) {
        super(EntityType.CREEPER, ((CraftWorld) loc.getWorld()).getHandle());
    }

    @Override
    public String entityID() {
        return "CREEPER";
    }

    @Override
    public String entityName() {
        return "Creeper";
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
        return "creeper";
    }
}

