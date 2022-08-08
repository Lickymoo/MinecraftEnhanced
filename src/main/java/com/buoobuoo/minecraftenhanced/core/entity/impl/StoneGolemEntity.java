package com.buoobuoo.minecraftenhanced.core.entity.impl;

import com.buoobuoo.minecraftenhanced.core.entity.interf.ModelEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

public class StoneGolemEntity extends IronGolem implements ModelEntity {

    public StoneGolemEntity(Location loc) {
        super(EntityType.IRON_GOLEM, ((CraftWorld) loc.getWorld()).getHandle());
    }

    @Override
    public String entityID() {
        return "STONE_GOLEM";
    }

    @Override
    public String entityName() {
        return "Stone Golem";
    }

    @Override
    public double maxHealth() {
        return 1;
    }

    @Override
    public double damage() {
        return 0;
    }

    @Override
    public double tagOffset() {
        return 0;
    }

    @Override
    public int entityLevel() {
        return 1;
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
        return "stonegolem";
    }
}
