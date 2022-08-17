package com.buoobuoo.minecraftenhanced.core.area.impl.aramore;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.Area;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class AramoreAbandonedShrineArea extends Area {
    public AramoreAbandonedShrineArea() {
        super("Abandoned Shrine",
                BoundingBox.of(
                        new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), -33, 90, -81),
                        new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), -86, 131, -133)
                ));
    }
}
