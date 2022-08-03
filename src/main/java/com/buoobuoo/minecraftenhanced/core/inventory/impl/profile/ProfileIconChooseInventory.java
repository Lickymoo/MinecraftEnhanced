package com.buoobuoo.minecraftenhanced.core.inventory.impl.profile;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
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

public class ProfileIconChooseInventory extends CustomInventory {
    private UUID uuid;

    public ProfileIconChooseInventory(MinecraftEnhanced plugin, Player player, UUID profileUUID) {
        super(plugin, player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PROFILE_ICON_EDIT + UnicodeSpaceUtil.getNeg(169)
                +"&8Profile Icon Selection", 54);
        this.uuid = profileUUID;

        this.addDefaultHandler(event -> {
            if(event.getCurrentItem() == null)
                return;
            ItemStack item = event.getCurrentItem();

            ProfileData profileData = plugin.getPlayerManager().getProfile(uuid);

            profileData.setProfileIcon(item.getType());
            profileData.save(plugin);

            Inventory inv = new ProfileEditInventory(plugin, player, uuid).getInventory();
            player.openInventory(inv);
        });

        this.addHandler(event -> {
            Inventory i = new ProfileEditInventory(plugin, player, uuid).getInventory();
            player.openInventory(i);
        }, 0);
    }


    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));
        Material mats[] = new Material[]{
                Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_SHOVEL, Material.WOODEN_PICKAXE
        };
        int index = 9;
        for(Material mat : mats){
            ItemStack item = new ItemBuilder(mat).name("&7Click to select as profile icon").create();
            inv.setItem(index++, item);
        }

        ItemStack back = new ItemBuilder(Material.PAPER).setCustomModelData(1000).name("&7Return to profile edit").create();
        inv.setItem(0, back);

        return inv;
    }
}









































