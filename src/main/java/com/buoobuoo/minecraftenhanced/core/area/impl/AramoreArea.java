package com.buoobuoo.minecraftenhanced.core.area.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.Area;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class AramoreArea extends Area {

    public AramoreArea() {
        super("Aramore",
                BoundingBox.of(
                        new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 207, 60, 115),
                        new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 113, 300, 10)
                        )
        );
    }
}
