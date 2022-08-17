package com.buoobuoo.minecraftenhanced.core.player;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.permission.PermissionGroup;
import com.buoobuoo.minecraftenhanced.persistence.serialization.DoNotSerialize;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class PlayerData {
    @DoNotSerialize
    private MinecraftEnhanced plugin;

    private UUID ownerID;
    private List<UUID> profileIDs = new ArrayList<>();
    private PermissionGroup group = PermissionGroup.MEMBER;
    private int maxProfiles = 7;

    private List<UUID> friends = new ArrayList<>();
    private List<UUID> friendRequestsReceived = new ArrayList<>();

    @DoNotSerialize
    private UUID activeProfileID;

    public void deleteProfile(UUID uuid){
        profileIDs.remove(uuid);
    }

    public UUID createProfile(){
        UUID uuid = UUID.randomUUID();
        profileIDs.add(uuid);
        return uuid;
    }

    public boolean setCurrentProfile(UUID profileID){
        Player player = Bukkit.getPlayer(ownerID);
        if(activeProfileID != null){
            save(plugin);
            plugin.getEntityManager().cleanUp(plugin.getPlayerManager().getProfile(activeProfileID));
            plugin.getPlayerManager().removeProfile(activeProfileID);
            plugin.getQuestManager().clearPlayer(player);

            player.resetPlayerWeather();
        }

        activeProfileID = null;
        player.getInventory().clear();

        if(profileID == null)
            return true;

        if(!profileIDs.contains(profileID))
            return false;

        ProfileData profileData = plugin.getPlayerManager().getProfile(profileID);
        profileData.setOwnerID(ownerID);
        activeProfileID = profileID;
        profileData.applyPlayer(player);

        return true;
    }

    public static PlayerData load(MinecraftEnhanced plugin, UUID playerID){
        //load from db
        PlayerData playerData = plugin.getMongoHook().loadObject(playerID.toString(), PlayerData.class, "playerData");
        playerData.plugin = plugin;
        return playerData;
    }

    public void save(MinecraftEnhanced plugin){
        if(activeProfileID != null){
            ProfileData profileData = plugin.getPlayerManager().getProfile(activeProfileID);
            profileData.save(plugin);
        }
        plugin.getMongoHook().saveObject(ownerID.toString(), this, "playerData");
    }

    public boolean isProfileCapacity(){
        return (profileIDs.size() >= maxProfiles);
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(ownerID);
    }

}
