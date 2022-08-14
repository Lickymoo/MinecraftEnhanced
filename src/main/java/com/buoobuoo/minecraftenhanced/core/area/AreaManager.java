package com.buoobuoo.minecraftenhanced.core.area;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.impl.aramore.AramoreArea;
import com.buoobuoo.minecraftenhanced.core.area.impl.aramore.AramoreWolfArea;
import com.buoobuoo.minecraftenhanced.core.event.AreaEnterEvent;
import com.buoobuoo.minecraftenhanced.core.event.AreaExitEvent;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateSecondEvent;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class AreaManager implements Listener {

    private final MinecraftEnhanced plugin;
    private final List<Area> areaList = new ArrayList<>();

    public AreaManager(MinecraftEnhanced plugin){
        this.plugin = plugin;

        registerAreas(
                //aramore area
                new AramoreArea(),
                new AramoreWolfArea()

        );
    }

    public void registerAreas(Area... areas){
        areaList.addAll(List.of(areas));
    }

    public Area getArea(Location loc){
        //TODO
        Area selectedArea = null;
        for(Area area : areaList){
            if(area.contains(loc)){
                selectedArea = area;
            }
        }
        return selectedArea;
    }

    public Area getAreaByName(String name){
        for(Area area : areaList){
            if(area.getName().equalsIgnoreCase(name))
                return area;
        }
        return null;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(event.getFrom().distance(event.getTo()) == 0)
            return;

        Player player = event.getPlayer();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);

        Location loc = player.getLocation();
        Area area = getArea(loc);

        if(profileData.getCurrentArea() == area)
            return;

        if(area == null)
            exitArea(profileData, profileData.getCurrentArea());
        else
            enterArea(profileData, area);
    }

    @EventHandler
    public void updateSecond(UpdateSecondEvent event){
        for(Area area : areaList){
            if(!(area instanceof MobSpawningArea mobSpawningArea))
                continue;

            mobSpawningArea.updateSecond(plugin);
        }
    }

    public void enterArea(ProfileData profileData, Area area){
        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        profileData.setCurrentArea(area);
        if(area instanceof TownArea) {
            player.sendTitle("", Util.formatColour("&7&lEntering " + area.getName()));
            profileData.setLastAreaName(area.getName());
        }

        AreaEnterEvent event = new AreaEnterEvent(player, area);
        Bukkit.getPluginManager().callEvent(event);

    }

    public void exitArea(ProfileData profileData, Area area){
        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        profileData.setCurrentArea(null);
        if(area instanceof TownArea) {
            player.sendTitle("", Util.formatColour("&7&lExiting " + area.getName()));
        }

        AreaExitEvent event = new AreaExitEvent(player, area);
        Bukkit.getPluginManager().callEvent(event);
    }
}




















































