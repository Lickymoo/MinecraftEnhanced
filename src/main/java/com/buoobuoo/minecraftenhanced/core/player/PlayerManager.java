package com.buoobuoo.minecraftenhanced.core.player;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateSecondEvent;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateTickEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.impl.profile.ProfileInventory;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
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

    private final Map<UUID, ProfileData> profileDataMap = new HashMap<>();
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerData getPlayer(Player player){
        return getPlayer(player.getUniqueId());
    }

    public PlayerData getPlayer(UUID uuid){
        if(playerDataMap.containsKey(uuid))
            return playerDataMap.get(uuid);

        PlayerData playerData = PlayerData.load(plugin, uuid);
        playerDataMap.put(uuid, playerData);

        return playerData;
    }

    public ProfileData getProfile(Player player){
        PlayerData data = getPlayer(player);
        return getProfile(data.getActiveProfileID());
    }

    public ProfileData getProfile(UUID uuid){
        if(profileDataMap.containsKey(uuid))
            return profileDataMap.get(uuid);

        ProfileData profileData = ProfileData.load(plugin, uuid);
        profileDataMap.put(uuid, profileData);

        return profileData;
    }

    //Rank tag


    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event){
        //Cache user data before join so they dont wait as long
        UUID uuid = event.getUniqueId();
        PlayerData data = getPlayer(uuid);
        data.setOwnerID(uuid);
    }

    @EventHandler(priority =  EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerData playerData = getPlayer(player);

        player.getInventory().clear();
        player.setHealth(20);

        //LOAD PLAYER INVENTORY
        Inventory inv = new ProfileInventory(plugin, player).getInventory();
        player.openInventory(inv);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        //save data
        PlayerData playerData = getPlayer(event.getPlayer());
        playerData.save(plugin);

        //remove all old profiles
        for(UUID uuid : playerData.getProfileIDs()){
            removeProfile(uuid);
        }

        playerDataMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onClose(UpdateSecondEvent event){
        for(Player player : Bukkit.getOnlinePlayers()){
            PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
            if(playerData.getActiveProfileID() != null)
                continue;

            Inventory inv = player.getOpenInventory().getTopInventory();

            if(plugin.getCustomInventoryManager().getRegistry().getHandler(inv) == null){
                Inventory i = new ProfileInventory(plugin, player).getInventory();
                player.openInventory(i);
            }

        }
    }

    public boolean isProfileLoaded(UUID uuid){
        return profileDataMap.containsKey(uuid);
    }

    @EventHandler
    public void updateSecond(UpdateSecondEvent event){
        for(PlayerData data : playerDataMap.values()){
            setDisplayPrefix(data);
        }
    }

    @EventHandler
    public void updateTick(UpdateTickEvent event){
        for(PlayerData data : playerDataMap.values()){
            if(data.getActiveProfileID() == null)
                continue;

            Player player = Bukkit.getPlayer(data.getOwnerID());
            ProfileData profileData = getProfile(data.getActiveProfileID());

            checkHealth(player, profileData);

        }
    }

    private void checkHealth(Player player, ProfileData profileData) {

        double health = profileData.getHealth();

        //TODO
        double maxHealth = ProfileData.BASE_HEALTH;

        //DEATH
        if (health <= 0) {
            player.setHealth(0);
            profileData.setHealth(maxHealth);
            return;
        }
        if (!player.isDead()) {
            player.setHealth((health / maxHealth) * 20);
        }
    }

    private void setDisplayPrefix(PlayerData playerData) {
        Player player = Bukkit.getPlayer(playerData.getOwnerID());

        if(playerData.getActiveProfileID() == null)
            return;

        ProfileData profileData = getProfile(player);
        try {
            Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
            Team team = scoreboard.getTeam(player.getName());

            if(team == null)
                team = scoreboard.registerNewTeam(player.getName());

            String prefix = CharRepo.numToTagString(profileData.getLevel());
            prefix = prefix + (playerData.getGroup().getGroupPrefix() != null ? playerData.getGroup().getGroupPrefix() + " " : "");
            team.setPrefix(Util.formatColour(prefix));
            team.addEntry(player.getName());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void removeProfile(UUID uuid){
        profileDataMap.remove(uuid);
    }
}



















