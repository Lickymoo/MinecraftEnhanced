package com.buoobuoo.minecraftenhanced.core.entity.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.entity.pathfinding.ReturnToOriginGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

public class ViciousWolfEntity extends Wolf implements CustomEntity {
    public ViciousWolfEntity(Location loc) {
        super(EntityType.WOLF, ((CraftWorld) loc.getWorld()).getHandle());
        this.setAggressive(true);
    }

    @Override
    public void registerGoals(){
        this.goalSelector.addGoal(0, new ReturnToOriginGoal(MinecraftEnhanced.getInstance().getEntityManager(), this, this, 1.5));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, 4, true, false, e -> {
            if(getOrigin() == null)
                return true;

            Location origin = getOrigin().getLeft();
            int maxRange = getOrigin().getRight();

            LivingEntity ent = e;
            Location entityLoc = ent.getBukkitEntity().getLocation();

            return origin.distance(entityLoc) <= maxRange;
        }));

        //this.targetSelector.addGoal(7, new NearestAttackableTargetGoal(this, AbstractSkeleton.class, false));
    }

    @Override
    public String entityID() {
        return "VICIOUS_WOLF";
    }

    @Override
    public String entityName() {
        return "Vicious Wolf";
    }

    @Override
    public double maxHealth() {
        return 15;
    }

    @Override
    public double damage() {
        return 2;
    }

    @Override
    public double tagOffset() {
        return -1;
    }

    @Override
    public int entityLevel() {
        return 2;
    }

    @Override
    public boolean showHealth() {
        return true;
    }

    @Override
    public String overrideTag() {
        return null;
    }
}


