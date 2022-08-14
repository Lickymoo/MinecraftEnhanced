package com.buoobuoo.minecraftenhanced.core.inventory.impl.profile;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.AnvilRenameEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;
import java.util.function.Consumer;

public class ProfileRenameInventory extends CustomInventory {
    private final Consumer<String> consumer;
    private final UUID profileUUID;

    public ProfileRenameInventory(MinecraftEnhanced plugin, Player player, UUID profileUUID, Consumer<String> consumer){
        super(plugin, player, UnicodeSpaceUtil.getNeg(60)
                + "&r&f" + CharRepo.UI_INVENTORY_PLAYER_SEARCH_OVERLAY
                + UnicodeSpaceUtil.getNeg(117)
                +"&8Enter profile name", 27);
        this.profileUUID = profileUUID;

        this.consumer = consumer;
        slotMap.put(2, event -> {
            if(event.getInventory().getItem(2) == null){
                return;
            }
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);

            ItemStack item = event.getInventory().getItem(2);
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            String name = pdc.get(new NamespacedKey(plugin, "PROFILE_SEL"), PersistentDataType.STRING);

            consumer.accept(name);
        });
    }

    @Override
    public Inventory getInventory() {
        ProfileData profileData = plugin.getPlayerManager().getProfile(profileUUID);
        ItemStack item = new ItemBuilder(MatRepo.INVISIBLE).name(profileData.getProfileName()).create();

        Inventory inv = Bukkit.createInventory(this, InventoryType.ANVIL, Util.formatColour(title));
        inv.setItem(0, item);

        ItemStack invis = new ItemBuilder(MatRepo.INVISIBLE).name(" ").create();
        inv.setItem(1,invis);

        return inv;
    }

    @EventHandler
    public void onRename(AnvilRenameEvent event){
        if(event.getHandler() != this)
            return;

        String name = event.getName().trim();

        ItemStack accept = new ItemBuilder(MatRepo.GREEN_TICK).name(Util.formatColour("&a&lRename Profile")).create();
        ItemMeta meta = accept.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, "PROFILE_SEL"), PersistentDataType.STRING, name);
        accept.setItemMeta(meta);

        Inventory inv = event.getPlayer().getOpenInventory().getTopInventory();
        inv.setItem(2,accept);

    }
}
