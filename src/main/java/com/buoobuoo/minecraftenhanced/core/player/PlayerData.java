package com.buoobuoo.minecraftenhanced.core.player;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.permission.PermissionGroup;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    private UUID profileID;
    private UUID ownerID;

    private int level;
    private int experience;

    private List<String> completedQuest = new ArrayList<>();
    private List<String> activeQuests = new ArrayList<>();

    private PermissionGroup group = PermissionGroup.MEMBER;
    private ItemStack[] inventoryContents;
    private ItemStack[] armorContents;

    public static PlayerData load(MinecraftEnhanced plugin, UUID uuid){
        //load from db
        return plugin.getMongoHook().loadObject(uuid.toString(), PlayerData.class, "players");
    }

    public void save(MinecraftEnhanced plugin){
        Player player = Bukkit.getPlayer(ownerID);
        inventoryContents = player.getInventory().getContents();
        armorContents = player.getInventory().getArmorContents();
        plugin.getMongoHook().saveObject(ownerID.toString(), this, "players");
    }
}
