package com.buoobuoo.minecraftenhanced.core.navigation;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class Route {
    private final Location[] routePoints;
    private Runnable onComplete;

    public Route(Location... routePoints){
        this.routePoints = routePoints;
    }
}
