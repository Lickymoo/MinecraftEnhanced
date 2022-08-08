package com.buoobuoo.minecraftenhanced.core.navigation;

import lombok.Getter;
import org.bukkit.Location;

@Getter
public class Route {
    private final Location[] routePoints;

    public Route(Location... routePoints){
        this.routePoints = routePoints;
    }
}
