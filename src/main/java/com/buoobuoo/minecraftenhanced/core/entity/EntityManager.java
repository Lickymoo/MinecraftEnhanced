package com.buoobuoo.minecraftenhanced.core.entity;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import lombok.Getter;
import net.minecraft.world.entity.monster.Enemy;
import org.bukkit.Location;
import net.minecraft.world.entity.Entity;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class EntityManager {

    private final MinecraftEnhanced plugin;

    public EntityManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    private List<Enemy> enemies = new ArrayList<>();

    public void spawnEntity(Location loc, Class<? extends Entity> entityClass) {
        try {
            Entity ent = entityClass.getConstructor(Location.class).newInstance(loc);
            PersistentDataContainer pdc = ent.getBukkitEntity().getPersistentDataContainer();
            pdc.set(new NamespacedKey(plugin, "ENT_ID"), PersistentDataType.STRING, "CUSTOM");
            pdc.set(new NamespacedKey(plugin, "HEALTH"), PersistentDataType.DOUBLE, 1000d);

            ent.getCommandSenderWorld().addFreshEntity(ent);
        }catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){

        }
    }
}
