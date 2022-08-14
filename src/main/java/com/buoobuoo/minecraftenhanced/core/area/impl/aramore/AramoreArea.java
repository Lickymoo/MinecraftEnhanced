package com.buoobuoo.minecraftenhanced.core.area.impl.aramore;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.TownArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class AramoreArea extends TownArea {

    public AramoreArea() {
        super("Aramore",
                new Location(MinecraftEnhanced.getMainWorld(), 167, 66, 63),
                BoundingBox.of(
                        new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 207, 60, 115),
                        new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 113, 300, 10)
                        )
        );
    }
}
