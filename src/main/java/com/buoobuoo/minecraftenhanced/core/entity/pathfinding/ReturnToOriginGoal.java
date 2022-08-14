package com.buoobuoo.minecraftenhanced.core.entity.pathfinding;

import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.util.Pair;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nullable;

public class ReturnToOriginGoal extends Goal {

    private final EntityManager entityManager;

    protected final PathfinderMob mob;
    protected final double speed;
    protected final PathNavigation pathNav;
    protected final CustomEntity entity;

    @Nullable
    protected Path path;

    public ReturnToOriginGoal(EntityManager entityManager, CustomEntity entity, PathfinderMob mob, double speed) {
        this.entityManager = entityManager;
        this.mob = mob;
        this.speed = speed;
        this.pathNav = mob.getNavigation();
        this.entity = entity;
    }

    public boolean canUse() {
        Location currentLoc = new Location(mob.getBukkitEntity().getWorld(), mob.getX(), mob.getY(), mob.getZ());
        Pair<Location, Integer> origin = entity.getOrigin();
        if(origin == null)
            return false;

        Location originPoint = origin.getLeft();
        if(currentLoc.distance(originPoint) <= origin.getRight())
            return false;

        this.path = this.pathNav.createPath(originPoint.getX(), originPoint.getY(), originPoint.getZ(), 0);

        return path != null;
    }

    public boolean canContinueToUse() {
        return !this.pathNav.isDone();
    }

    public void start() {
        this.pathNav.moveTo(this.path, this.speed);
    }

    public void stop() {
    }

    public void tick() {
        this.mob.getNavigation().setSpeedModifier(this.speed);
    }
}