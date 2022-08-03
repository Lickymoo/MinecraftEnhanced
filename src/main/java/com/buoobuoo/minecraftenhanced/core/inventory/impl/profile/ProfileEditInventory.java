package com.buoobuoo.minecraftenhanced.core.inventory.impl.profile;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ProfileEditInventory extends CustomInventory {
    private UUID uuid;

    public ProfileEditInventory(MinecraftEnhanced plugin, Player player, UUID profileUUID) {
        super(plugin, player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PROFILE_EDIT_MAIN + UnicodeSpaceUtil.getNeg(169)
                +"&8Profile Edit", 27);
        this.uuid = profileUUID;

        this.addHandler(event -> {
            Inventory i = new ProfileDeleteInventory(plugin, player, profileUUID, pl -> {
                Inventory i2 = new ProfileEditInventory(plugin, pl, profileUUID).getInventory();
                pl.openInventory(i2);
            }).getInventory();
            player.openInventory(i);
        }, 26);

        this.addHandler(event -> {
            Inventory i = new ProfileInventory(plugin, player).getInventory();
            player.openInventory(i);
        }, 0);

        this.addHandler(event -> {
            Inventory i = new ProfileIconChooseInventory(plugin, player, profileUUID).getInventory();
            player.openInventory(i);
        }, 13);

        this.addHandler(event -> {
            Inventory i = new ProfileRenameInventory(plugin, player, profileUUID, name -> {
                ProfileData profileData = plugin.getPlayerManager().getProfile(uuid);
                profileData.setProfileName(name);
                profileData.save(plugin);

                Inventory i2 = new ProfileEditInventory(plugin, player, profileUUID).getInventory();
                player.openInventory(i2);
            }).getInventory();
            player.openInventory(i);
        }, 17);
    }


    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));
        PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
        ProfileData profileData = plugin.getPlayerManager().getProfile(uuid);

        ItemStack back = new ItemBuilder(Material.PAPER).setCustomModelData(1000).name("&7Return to profiles").create();
        inv.setItem(0, back);

        ItemStack rename = new ItemBuilder(Material.PAPER).setCustomModelData(1000).name("&7Rename Profile").lore("&7Current: &r&f" + profileData.getProfileName()).create();
        inv.setItem(17, rename);

        ItemStack delete = new ItemBuilder(Material.PAPER).setCustomModelData(1000).name("&7Delete Profile").create();
        inv.setItem(26, delete);

        ItemStack icon = new ItemBuilder(profileData.getProfileIcon()).name("&7Click to change profile icon").create();
        inv.setItem(13, icon);

        return inv;
    }
}









































