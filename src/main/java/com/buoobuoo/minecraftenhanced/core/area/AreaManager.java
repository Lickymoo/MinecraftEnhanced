package com.buoobuoo.minecraftenhanced.core.area;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.impl.AramoreArea;
import com.buoobuoo.minecraftenhanced.core.event.AreaEnterEvent;
import com.buoobuoo.minecraftenhanced.core.event.AreaExitEvent;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
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
            new AramoreArea()

        );
    }

    public void registerAreas(Area... areas){
        areaList.addAll(List.of(areas));
    }

    public Area getArea(Location loc){
        for(Area area : areaList){
            if(area.contains(loc))
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

    public void enterArea(ProfileData profileData, Area area){
        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        profileData.setCurrentArea(area);
        player.sendMessage("Entering " + area.getName());

        AreaEnterEvent event = new AreaEnterEvent(player, area);
        Bukkit.getPluginManager().callEvent(event);

    }

    public void exitArea(ProfileData profileData, Area area){
        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        profileData.setCurrentArea(null);
        player.sendMessage("Exiting " + area.getName());

        AreaExitEvent event = new AreaExitEvent(player, area);
        Bukkit.getPluginManager().callEvent(event);
    }
}




















































