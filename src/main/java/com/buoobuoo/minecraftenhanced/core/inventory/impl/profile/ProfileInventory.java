package com.buoobuoo.minecraftenhanced.core.inventory.impl.profile;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.inventory.impl.logout.LogoutInventory;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class ProfileInventory extends CustomInventory {

    private final boolean capacity;

    public ProfileInventory(MinecraftEnhanced plugin, Player player) {
        this(plugin, player, plugin.getPlayerManager().getPlayer(player).isProfileCapacity());
    }

    public ProfileInventory(MinecraftEnhanced plugin, Player player, boolean capacity) {
        super(plugin, player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + (capacity ? CharRepo.UI_INVENTORY_PROFILE_SELECT_FULL : CharRepo.UI_INVENTORY_PROFILE_SELECT_MAIN) + UnicodeSpaceUtil.getNeg(169)
                +"&8Profile Select", 36);

        this.capacity = capacity;

        this.addHandler(event -> {
            if(event.getCurrentItem() == null)
                return;

            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            String id = pdc.get(new NamespacedKey(plugin, "profileID"), PersistentDataType.STRING);

            if(event.isRightClick()){
                Inventory inv = new ProfileEditInventory(plugin, player, UUID.fromString(id)).getInventory();
                player.openInventory(inv);
                return;
            }

            UUID uuid = UUID.fromString(id);

            //If it fails, return
            if(!plugin.getPlayerManager().getPlayer(player).setCurrentProfile(uuid))
                return;

            player.closeInventory();

        }, 10, 11, 12, 13, 14, 15, 16);

        if(!capacity) {
            this.addHandler(event -> {
                PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
                UUID profileID = playerData.createProfile();
                ProfileData profileData = plugin.getPlayerManager().getProfile(profileID);
                profileData.init(player);
                profileData.save(plugin);

                Inventory i = new ProfileInventory(plugin, player).getInventory();
                player.openInventory(i);
            }, 30, 31, 32);
        }

        this.addHandler(event -> {
            Inventory inv = new LogoutInventory(plugin, player, pl -> {
                Inventory i = new ProfileInventory(plugin, pl).getInventory();
                pl.openInventory(i);
            }).getInventory();
            player.openInventory(inv);
        }, 27, 28);
    }


    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));
        PlayerData playerData = plugin.getPlayerManager().getPlayer(player);

        int index = 10;
        for (UUID uuid : playerData.getProfileIDs()) {
            Material mat;
            String name;
            int level;

            if (!plugin.getPlayerManager().isProfileLoaded(uuid)) {
                mat = plugin.getMongoHook().getValue(uuid.toString(), "profileIcon", Material.class, "profileData");
                name = plugin.getMongoHook().getValue(uuid.toString(), "profileName", String.class, "profileData");
                level = plugin.getMongoHook().getValue(uuid.toString(), "level", Integer.class, "profileData");
            } else {
                mat = plugin.getPlayerManager().getProfile(uuid).getProfileIcon();
                name = plugin.getPlayerManager().getProfile(uuid).getProfileName();
                level = plugin.getPlayerManager().getProfile(uuid).getLevel();
            }

            mat = mat == null ? Material.WOODEN_SWORD : mat;

            ItemStack item = new ItemBuilder(mat).name("&r&f" + name).nbtString(plugin, "profileID", uuid.toString()).lore("&r&7Level &r&f" + level, " ", "&r&fRight click &7to edit profile", "&r&fLeft click &7to select profile").create();
            inv.setItem(index++, item);
        }

        if (!capacity){
            ItemStack create = new ItemBuilder(Material.PAPER).setCustomModelData(1000).name("&7Create Profile").create();
            inv.setItem(30, create);
            inv.setItem(31, create);
            inv.setItem(32, create);
        }

        ItemStack logout = new ItemBuilder(Material.PAPER).setCustomModelData(1000).name("&7Logout").create();
        inv.setItem(27, logout);
        inv.setItem(28, logout);

        return inv;
    }
}









































