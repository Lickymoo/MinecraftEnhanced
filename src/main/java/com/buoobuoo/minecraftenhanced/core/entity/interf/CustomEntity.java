package com.buoobuoo.minecraftenhanced.core.entity.interf;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.nio.file.Path;

public interface CustomEntity {

    String entityID();
    String entityName();
    String overrideTag();
    double maxHealth();
    double damage();
    double tagOffset();
    int entityLevel();
    boolean showHealth();

    //abstract
    default void onHit(ProfileData profileData){}

    default void onDeath(){}

    default void onSpawn(){}

    //inbuilt
    default double calculateExpDrop(int n){
        int level = entityLevel();
        double g = 25 * level * (1 + level);
        double f = (g/(25*level))*level-Math.abs(level-n)*level;
        return f;
    }

    default void spawnEntity(MinecraftEnhanced plugin, Location loc){
        Entity ent = asEntity();

        ent.setPos(loc.getX(), loc.getY(), loc.getZ());

        PersistentDataContainer pdc = ent.getBukkitEntity().getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, "ENT_ID"), PersistentDataType.STRING, entityID());
        pdc.set(new NamespacedKey(plugin, "HEALTH"), PersistentDataType.DOUBLE, maxHealth());
        pdc.set(new NamespacedKey(plugin, "DAMAGE"), PersistentDataType.DOUBLE, damage());

        ent.getCommandSenderWorld().addFreshEntity(ent);

        //Spawn hologram for tags

        onSpawn();
    }

    default void entityTick(){
    }

    default Entity asEntity(){
        return (Entity)this;
    }

    default void lookAt(Location loc){
        Location lookDir = loc.clone();
        lookDir.setDirection(loc.subtract(lookDir).toVector());
        moveTo(lookDir);
    }

    default void moveTo(Location loc){
        PathfinderMob pathfinderMob = getPathfinderMob();
        pathfinderMob.moveTo(loc.getX(), loc.getY(), loc.getZ());
    }

    default PathfinderMob getPathfinderMob(){
        return (PathfinderMob) asEntity();
    }
}












































