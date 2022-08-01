package com.buoobuoo.minecraftenhanced.core.player;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.UpdateSecondEvent;
import com.buoobuoo.minecraftenhanced.core.permission.PermissionManager;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager implements Listener {
    private final MinecraftEnhanced plugin;

    public PlayerManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    private Map<UUID, PlayerData> playerList = new HashMap<>();

    public PlayerData getPlayer(Player player){
        return getPlayer(player.getUniqueId());
    }

    public PlayerData getPlayer(UUID uuid){
        if(playerList.containsKey(uuid))
            return playerList.get(uuid);

        PlayerData playerData = PlayerData.load(plugin, uuid);
        playerList.put(uuid, playerData);

        return playerData;
    }

    //Rank tag

    @EventHandler
    private void prefixUpdateEvent(UpdateSecondEvent event) {
        for(Player player : Bukkit.getOnlinePlayers()){
            setDisplayPrefix(player);
        }
    }

    private void setDisplayPrefix(Player player){
        PermissionManager permissionManager = plugin.getPermissionManager();
        PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
        try {
            Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
            Team team = scoreboard.getTeam(player.getName());

            if(team == null)
                team = scoreboard.registerNewTeam(player.getName());

            team.setPrefix(Util.formatColour(playerData.getGroup().getGroupPrefix() != null ? playerData.getGroup().getGroupPrefix() + " ": ""));
            team.addEntry(player.getName());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAll(){
        for(PlayerData data : playerList.values()){
            data.save(plugin);
        }
    }

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event){
        //Cache user data before join so they dont wait as long
        UUID uuid = event.getUniqueId();
        PlayerData data = getPlayer(uuid);
        data.setOwnerID(uuid);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerData data = getPlayer(player);

        if(data.getInventoryContents() != null)
            player.getInventory().setContents(data.getInventoryContents());
        if(data.getArmorContents() != null)
            player.getInventory().setArmorContents(data.getArmorContents());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        //save data
        PlayerData playerData = getPlayer(event.getPlayer().getUniqueId());
        playerData.save(plugin);

        playerList.remove(event.getPlayer().getUniqueId());
    }
}



















