package com.buoobuoo.minecraftenhanced.core.inventory.impl.profile;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
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
import java.util.function.Consumer;

public class ProfileDeleteInventory extends CustomInventory {
    private Consumer<Player> noOption;
    private UUID uuid;

    public ProfileDeleteInventory(MinecraftEnhanced plugin, Player player, UUID profileUUID, Consumer<Player> noOption) {
        super(plugin, player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PROFILE_CONFIRM_DELETE + UnicodeSpaceUtil.getNeg(169), 36);
        this.uuid = profileUUID;

        this.addHandler(event -> {
            noOption.accept(player);
        }, 19, 20);

        this.addHandler(event -> {
            plugin.getPlayerManager().getPlayer(player).deleteProfile(profileUUID);
            Inventory i = new ProfileInventory(plugin, player).getInventory();
            player.openInventory(i);
        }, 24, 25);
    }


    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));

        ItemStack yes = new ItemBuilder(Material.PAPER).setCustomModelData(1000).name("&7No").lore("&r&fClick to return").create();
        inv.setItem(19, yes);
        inv.setItem(20, yes);

        ItemStack no = new ItemBuilder(Material.PAPER).setCustomModelData(1000).name("&cYes").lore("&r&fClick to delete profile").create();
        inv.setItem(24, no);
        inv.setItem(26, no);

        return inv;
    }
}









































