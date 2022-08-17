package com.buoobuoo.minecraftenhanced.core.player;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityCastType;
import com.buoobuoo.minecraftenhanced.core.area.Area;
import com.buoobuoo.minecraftenhanced.core.entity.EntityStatInstance;
import com.buoobuoo.minecraftenhanced.core.player.tempmodifier.TemporaryStatModifier;
import com.buoobuoo.minecraftenhanced.core.status.StatusEffect;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.persistence.serialization.DoNotSerialize;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
public class ProfileData {

    public static final double BASE_HEALTH = 20;
    public static final double BASE_MANA = 20;

    public static final double BASE_MANA_REGEN_PER_SECOND = .5;
    public static final double BASE_HEALTH_REGEN_PER_SECOND = .5;

    public static final double BASE_WALK_SPEED = 0.2f;

    private Material profileIcon = Material.WOODEN_SWORD;
    private String profileName;

    private UUID profileID;
    private UUID ownerID;

    private int level = 1;
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

    private String[] abilityIDs = new String[4];
    private AbilityCastType[] abilityCastTypes = new AbilityCastType[4];

    private String lastAreaName;

    @DoNotSerialize
    private HashMap<String, Integer> cooldowns;

    //stats
    private double health;
    private double mana;

    @DoNotSerialize
    private EntityStatInstance statInstance;

    @DoNotSerialize
    private List<StatusEffect> statusEffects = new ArrayList<>();

    @DoNotSerialize
    private ConcurrentLinkedQueue<TemporaryStatModifier> temporaryStatModifiers = new ConcurrentLinkedQueue<>();

    @DoNotSerialize
    private ConcurrentLinkedQueue<TemporaryStatModifier> onHitStatModifier = new ConcurrentLinkedQueue<>();

    @DoNotSerialize
    private Area currentArea;




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

    public void applyPlayer(Player player) {
        if (getInventoryContents() != null) {
            player.getInventory().setContents(getInventoryContents());
        }

        if (getArmorContents() != null) {
            player.getInventory().setArmorContents(getArmorContents());

        }

        if (getLocation() != null)
            player.teleport(getLocation());

        if (getGameMode() != null)
            player.setGameMode(getGameMode());

        player.getInventory().setHeldItemSlot(getSelectedSlot());
        player.setFlying(isFlying());

        ItemStack playerManagerItem = new ItemBuilder(Material.DIAMOND).name("&r&fPlayer Menu").lore("&7Click to open Player Menu").create();
        player.getInventory().setItem(8, playerManagerItem);

        player.updateInventory();

        MinecraftEnhanced.getInstance().getQuestManager().loadQuests(this);
    }

    public void init(Player player){
        setProfileName(player.getName() + "'s Profile");
        setOwnerID(player.getUniqueId());
        setHealth(BASE_HEALTH);
    }
}
