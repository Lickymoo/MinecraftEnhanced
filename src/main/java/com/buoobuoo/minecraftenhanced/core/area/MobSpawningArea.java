package com.buoobuoo.minecraftenhanced.core.area;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MobSpawningArea extends Area{
    protected final List<Location> mobSpawnPoint = new ArrayList<>();
    protected final List<Class<? extends CustomEntity>> entityTypes = new ArrayList<>();

    protected final Map<Location, CustomEntity> attributedEntity = new HashMap<>();

    public MobSpawningArea(String name, BoundingBox... boundingBoxes){
        super(name, boundingBoxes);
    }

    public void addMobSpawnPoint(Location loc){
        mobSpawnPoint.add(loc);
    }

    public void addEntityType(Class<? extends CustomEntity> type){
        entityTypes.add(type);
    }

    public void updateSecond(MinecraftEnhanced plugin){
        EntityManager entityManager = plugin.getEntityManager();

        for(Location loc : mobSpawnPoint){
            if(!loc.getChunk().isLoaded())
                continue;

            CustomEntity entity = attributedEntity.getOrDefault(loc, null);
            if(entity == null || entity.isDestroyed()){
                CustomEntity newEntity = entityManager.spawnEntity(entityTypes.get(0), loc);
                newEntity.setOriginPoint(loc, 12);
                newEntity.setDestroyOnUnload(true);
                attributedEntity.put(loc, newEntity);
            }
        }
    }
}
