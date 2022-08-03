package com.buoobuoo.minecraftenhanced.core.player;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.permission.PermissionGroup;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProfileData {

    private static final double BASE_HEALTH = 20;

    private Material profileIcon = Material.WOODEN_SWORD;
    private String profileName;

    private UUID profileID;
    private UUID ownerID;

    private int level = 0;
    private int experience = 0;

    private List<String> completedQuest = new ArrayList<>();
    private List<String> activeQuests = new ArrayList<>();

    private ItemStack[] inventoryContents;
    private ItemStack[] armorContents;
    private Location location;

    private GameMode gameMode;

    //stats
    private double health;


    public static ProfileData load(MinecraftEnhanced plugin, UUID id){
        //load from db
        ProfileData profileData = plugin.getMongoHook().loadObject(id.toString(), ProfileData.class, "profileData");
        profileData.setProfileID(id);
        return profileData;
    }

    public void save(MinecraftEnhanced plugin){
        Player player = Bukkit.getPlayer(ownerID);
        if(profileIcon == null)
            profileIcon = Material.WOODEN_SWORD;

        inventoryContents = player.getInventory().getContents();
        armorContents = player.getInventory().getArmorContents();
        location = player.getLocation();

        gameMode = player.getGameMode();
        plugin.getMongoHook().saveObject(profileID.toString(), this, "profileData");
    }

    public void init(Player player){
        setProfileName(player.getName() + "'s Profile");
        setOwnerID(player.getUniqueId());
        setHealth(BASE_HEALTH);

    }
}
