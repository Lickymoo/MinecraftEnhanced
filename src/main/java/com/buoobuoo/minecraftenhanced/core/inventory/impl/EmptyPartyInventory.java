package com.buoobuoo.minecraftenhanced.core.inventory.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.party.PartyInviteUpdateEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.party.Party;
import com.buoobuoo.minecraftenhanced.core.party.PartyManager;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class EmptyPartyInventory extends CustomInventory {
    private final PartyManager partyManager;

    public EmptyPartyInventory(MinecraftEnhanced plugin, Player player){
        super(plugin, player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PARTY_EMPTY + UnicodeSpaceUtil.getNeg(169)
                +"&8Party Invites", 36);
        this.partyManager = plugin.getPartyManager();

        this.addDefaultHandler(event -> {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
            if(event.getCurrentItem() == null)
                return;

            ItemStack item = event.getCurrentItem();

            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            String playerUUID = pdc.get(new NamespacedKey(plugin, "PLAYER_SEL"), PersistentDataType.STRING);

            if(playerUUID == null)
                return;

            Player leader = Bukkit.getPlayer(UUID.fromString(playerUUID));

            if(leader != null) {
                partyManager.join(player, leader);
                Inventory inv = new PartyInventory(plugin, player, partyManager.getPartyByPlayer(player)).getInventory();
                player.openInventory(inv);
            }
        });

        this.addHandler(event -> {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
            partyManager.create(player);
            Inventory inv = new PartyInventory(plugin, player, partyManager.getPartyByPlayer(player)).getInventory();
            player.openInventory(inv);
        }, 27, 28);
    }

    @EventHandler
    public void partyUpdate(PartyInviteUpdateEvent event){
        if(player != event.getPlayer())
            return;
        player.openInventory(new EmptyPartyInventory(plugin, player).getInventory());

    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));

        ItemStack create = new ItemBuilder(MatRepo.INVISIBLE).name("&7Create Party").create();
        inv.setItem(27, create);
        inv.setItem(28, create);

        int index = 0;
        for(Party party : partyManager.getInvitedParties(player)){
            Player leader = Bukkit.getPlayer(party.getLeader());
            ItemStack head = new ItemBuilder(Material.PLAYER_HEAD).skullOwner(leader).name(leader.getDisplayName() + "'s Party").lore("&7Click to join party").create();

            ItemMeta meta = head.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(new NamespacedKey(plugin, "PLAYER_SEL"), PersistentDataType.STRING, leader.getUniqueId().toString());
            head.setItemMeta(meta);

            inv.setItem(index, head);
            index++;
        }

        return inv;
    }
}
