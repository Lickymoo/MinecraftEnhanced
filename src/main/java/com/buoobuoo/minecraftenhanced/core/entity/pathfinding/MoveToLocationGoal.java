package com.buoobuoo.minecraftenhanced.core.entity.pathfinding;

import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import org.bukkit.Location;

import javax.annotation.Nullable;

public class MoveToLocationGoal extends Goal {

    private final EntityManager entityManager;

    protected final PathfinderMob mob;
    protected Location endLocation;
    protected final double speed;
    protected final PathNavigation pathNav;

    @Nullable
    protected Path path;

    public MoveToLocationGoal(EntityManager entityManager, PathfinderMob mob, double speed) {
        this.entityManager = entityManager;
        this.mob = mob;
        this.speed = speed;
        this.pathNav = mob.getNavigation();
    }

    public boolean canUse() {
        this.endLocation = entityManager.getNavigationMap().getOrDefault(mob, null);
        if(this.endLocation == null)
            return false;

        this.path = this.pathNav.createPath(endLocation.getX(), endLocation.getY(), endLocation.getZ(), 0);

        return path != null;
    }

    public boolean canContinueToUse() {
        return !this.pathNav.isDone();
    }

    public void start() {
        this.pathNav.moveTo(this.path, this.speed);
    }

    public void stop() {
        entityManager.getNavigationMap().put(mob, null);
        this.endLocation = null;
    }

    public void tick() {
        this.mob.getNavigation().setSpeedModifier(this.speed);

    }
}