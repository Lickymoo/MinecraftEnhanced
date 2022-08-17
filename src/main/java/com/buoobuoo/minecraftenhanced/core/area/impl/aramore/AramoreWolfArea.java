package com.buoobuoo.minecraftenhanced.core.area.impl.aramore;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.MobSpawningArea;
import com.buoobuoo.minecraftenhanced.core.entity.impl.ViciousWolfEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class AramoreWolfArea extends MobSpawningArea {

    public AramoreWolfArea() {
        super("Aramore",
                BoundingBox.of(
                        new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 107, 81, -79),
                        new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 149, 67, -52)
                        )
        );

        addMobSpawnPoint(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 140, 67, -65));
        //addMobSpawnPoint(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 121, 67, -61));
        //addMobSpawnPoint(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 135, 68, -80));

        addEntityType(ViciousWolfEntity.class);
    }
}
