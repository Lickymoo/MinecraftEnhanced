package com.buoobuoo.minecraftenhanced.core.navigation;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RouteManager {

    private final MinecraftEnhanced plugin;
    private final List<Route> routeList = new ArrayList<>();

    public RouteManager(MinecraftEnhanced plugin){
        this.plugin = plugin;

        registerRoutes(
                new Route(
                        new Location(MinecraftEnhanced.getMainWorld(), 197, 66, 216),
                        new Location(MinecraftEnhanced.getMainWorld(), 195, 52, 269)

                )
        );
    }


    public void registerRoutes(Route... routes){
        for(Route route : routes){
            routeList.add(route);
        }
    }

    public ArrayDeque<Location> getRoutePoints(Route route){
        ArrayDeque<Location> deque = new ArrayDeque<>();


        for(int index = 1; index < route.getRoutePoints().length; index++) {
            Location loc1 = route.getRoutePoints()[index];
            Location loc2 = route.getRoutePoints()[index-1];

            double x1 = loc1.getX();
            double x2 = loc2.getX();

            double y1 = loc1.getY();
            double y2 = loc2.getY();

            double z1 = loc1.getZ();
            double z2 = loc2.getZ();

            //2d
            double dist = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(z1 - z2, 2));

            for (int i = 0; i < dist; i += 5) {
                double t = i / dist;
                double nX = (1 - t) * x2 + (t * x1);
                double nZ = (1 - t) * z2 + (t * z1);
                double nY = (1 - t) * y2 + (t * y1);

                Location loc = new Location(loc1.getWorld(), nX, nY, nZ);
                //get block loc
                loc = loc.getBlock().getLocation().add(.5, 0, .5);

                //find solid block
                while (loc.getBlock().isPassable()) {
                    loc.subtract(0, 1, 0);
                }

                //add 1
                loc.add(0, 1, 0);
                deque.add(loc);
            }
        }
        return deque;
    }

    public void showRoute(Player player){
        Route route = routeList.get(0);

        Location loc1 = route.getRoutePoints()[0];
        Location loc2 = route.getRoutePoints()[1];

        double x1 = loc1.getX();
        double x2 = loc2.getX();

        double y1 = loc1.getY();
        double y2 = loc2.getY();

        double z1 = loc1.getZ();
        double z2 = loc2.getZ();

        //2d
        double dist = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(z1-z2,2));
        double yDiff = Math.abs(y1 - y2);
        //get dist between points
        double t = (dist/2)/dist;
        double nX = (1-t)*x2 + (t*x1);
        double nZ = (1-t)*z2 + (t*z1);
        double nY = (1-t)*y2 + (t*y1);

        Bukkit.broadcastMessage("Mid x: " + nX + ", y: " + nY + ", z: " + nZ);
        Bukkit.broadcastMessage("yDiff: " + yDiff);
        Bukkit.broadcastMessage("Distance: " + dist);

        for(int i = 0; i < dist; i+= 1){
            t = i/dist;
            nX = (1-t)*x2 + (t*x1);
            nZ = (1-t)*z2 + (t*z1);
            nY = (1-t)*y2 + (t*y1);

            Location loc = new Location(loc1.getWorld(), nX, nY, nZ);
            loc = loc.getBlock().getLocation().add(.5, 0, .5);
            while(loc.getBlock().isPassable()){
                loc.subtract(0, 1, 0);
            }
            loc.add(0, 1, 0);

            BlockData blockData = Material.GOLD_BLOCK.createBlockData();
            player.sendBlockChange(loc, blockData);
        }
    }
}
