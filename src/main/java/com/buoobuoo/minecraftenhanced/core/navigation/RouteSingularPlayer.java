package com.buoobuoo.minecraftenhanced.core.navigation;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class RouteSingularPlayer extends Route{
    private Player player;
    private String routeID;

    public RouteSingularPlayer(Location... routePoints){
        super(routePoints);
    }
}
