package com.buoobuoo.minecraftenhanced.core.inventory.impl.logout;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class LogoutInventory extends CustomInventory {
    private Consumer<Player> noOption;

    public LogoutInventory(MinecraftEnhanced plugin, Player player, Consumer<Player> noOption) {
        super(plugin, player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_LOGOUT + UnicodeSpaceUtil.getNeg(169), 36);

        this.addHandler(event -> {
            noOption.accept(player);
        }, 19, 20);

        this.addHandler(event -> {
            PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
            playerData.save(plugin);

            player.kickPlayer(Util.formatColour("&7Thankyou for playing :D"));
        }, 24, 25);
    }


    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));

        ItemStack yes = new ItemBuilder(MatRepo.INVISIBLE).name("&7No").lore("&r&fClick to return").create();
        inv.setItem(19, yes);
        inv.setItem(20, yes);

        ItemStack no = new ItemBuilder(MatRepo.INVISIBLE).name("&cYes").lore("&r&fClick to log out").create();
        inv.setItem(24, no);
        inv.setItem(26, no);

        return inv;
    }
}









































