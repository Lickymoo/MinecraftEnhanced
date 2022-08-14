package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.Area;
import com.buoobuoo.minecraftenhanced.core.area.AreaManager;
import com.buoobuoo.minecraftenhanced.core.area.TownArea;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathEventListener implements Listener {

    private final MinecraftEnhanced plugin;

    public PlayerDeathEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(PlayerDeathEvent event){
        event.setDeathMessage(null);
    }

    @EventHandler
    public void respawnEvent(PlayerRespawnEvent event){
        Location defaultRespawn = new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 195, 51, 282, -180, 0);

        Player player = event.getPlayer();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);

        AreaManager areaManager = plugin.getAreaManager();

        String areaName = profileData.getLastAreaName();

        Area area = areaManager.getAreaByName(areaName);

        if(area instanceof TownArea townArea){
            event.setRespawnLocation(townArea.getRespawnPoint());
        }else{
            event.setRespawnLocation(defaultRespawn);
        }

    }
}
