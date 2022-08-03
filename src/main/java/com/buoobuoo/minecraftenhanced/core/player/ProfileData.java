package com.buoobuoo.minecraftenhanced.core.player;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
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

    public static final double BASE_HEALTH = 20;

    private Material profileIcon = Material.WOODEN_SWORD;
    private String profileName;

    private UUID profileID;
    private UUID ownerID;

    private int level = 0;
    private int experience = 0;

    private List<String> completedQuest = new ArrayList<>();
    private List<String> activeQuests = new ArrayList<>();


    //meta vars
    private ItemStack[] inventoryContents;
    private ItemStack[] armorContents;
    private Location location;
    private int selectedSlot;

    private GameMode gameMode;
    private boolean flying;

    //stats
    private double health;









    public static ProfileData load(MinecraftEnhanced plugin, UUID id){
        //load from db
        ProfileData profileData = plugin.getMongoHook().loadObject(id.toString(), ProfileData.class, "profileData");
        profileData.setProfileID(id);
        return profileData;
    }

    public void save(MinecraftEnhanced plugin, boolean metaData) {
        if (metaData){
            save(plugin);
            return;
        }

        plugin.getMongoHook().saveObject(profileID.toString(), this, "profileData");
    }

    public void save(MinecraftEnhanced plugin){
        Player player = Bukkit.getPlayer(ownerID);
        if(profileIcon == null)
            profileIcon = Material.WOODEN_SWORD;

        inventoryContents = player.getInventory().getContents();

        armorContents = player.getInventory().getArmorContents();
        location = player.getLocation();
        selectedSlot = player.getInventory().getHeldItemSlot();

        gameMode = player.getGameMode();
        flying = player.isFlying();

        plugin.getMongoHook().saveObject(profileID.toString(), this, "profileData");
    }

    public void applyPlayer(Player player){
        if(getInventoryContents() != null) {
            player.getInventory().setContents(getInventoryContents());
        }

        if(getArmorContents() != null) {
            player.getInventory().setArmorContents(getArmorContents());

        }

        if(getGameMode() != null)
            player.setGameMode(getGameMode());

        if(getLocation() != null)
            player.teleport(getLocation());

        player.getInventory().setHeldItemSlot(getSelectedSlot());
        player.setFlying(isFlying());
    }

    public void init(Player player){
        setProfileName(player.getName() + "'s Profile");
        setOwnerID(player.getUniqueId());
        setHealth(BASE_HEALTH);

    }
}
