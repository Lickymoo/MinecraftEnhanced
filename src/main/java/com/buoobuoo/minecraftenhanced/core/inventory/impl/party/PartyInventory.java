package com.buoobuoo.minecraftenhanced.core.inventory.impl.party;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.party.PartyUpdateEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.social.party.Party;
import com.buoobuoo.minecraftenhanced.core.social.party.PartyManager;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PartyInventory extends CustomInventory {
    private final Party party;
    private final PartyManager partyManager;

    public PartyInventory(MinecraftEnhanced plugin, Player player, Party party){
        super(plugin, player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + (party.isLeader(player) ? CharRepo.UI_INVENTORY_PARTY_MAIN_LEADER : CharRepo.UI_INVENTORY_PARTY_MAIN_MEMBER) + UnicodeSpaceUtil.getNeg(177) + CharRepo.UI_INVENTORY_PARTY_DECO_1, 36);
        boolean isLeader = party.isLeader(player);
        this.party = party;
        this.partyManager = plugin.getPartyManager();

        if(isLeader) {
            this.addHandler(event -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
                Inventory inv = new PlayerSearchInventory(plugin, player, target -> {
                    Inventory reopen = new PartyInventory(plugin, player, party).getInventory();
                    partyManager.invite(player, target);
                    player.openInventory(reopen);
                }).getInventory();
                player.openInventory(inv);
            }, 27, 28);
        }

        if(isLeader) {
            this.addHandler(event -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
                Inventory inv = new PlayerSearchInventory(plugin, player, party.getAllMembers(), target -> {
                    partyManager.promote(player, target);

                    Inventory reopen = new PartyInventory(plugin, player, party).getInventory();
                    player.openInventory(reopen);
                }).getInventory();
                player.openInventory(inv);
            }, 29, 30);
        }

        if(isLeader) {
            this.addHandler(event -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
                Inventory inv = new PlayerSearchInventory(plugin, player, party.getAllMembers(), target -> {
                    Inventory reopen = new PartyInventory(plugin, player, party).getInventory();
                    partyManager.kick(player, target);
                    player.openInventory(reopen);
                }).getInventory();
                player.openInventory(inv);
            }, 31, 32);
        }

        if(isLeader) {
            this.addHandler(event -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
                partyManager.disband(player);
                player.closeInventory();
            }, 34, 35);
        }else{
            this.addHandler(event -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
                partyManager.leave(player);
                player.closeInventory();
            }, 34, 35);
        }
    }

    @EventHandler
    public void partyUpdate(PartyUpdateEvent event){

        if(event.getParty().isDisbanded()){
            player.closeInventory();
            return;
        }

        if(party != event.getParty())
            return;

        player.openInventory(new PartyInventory(plugin, player, party).getInventory());

    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));
        boolean isLeader = party.isLeader(player);

        if(isLeader) {
            ItemStack invite = new ItemBuilder(MatRepo.INVISIBLE).name("&7Invite Player").create();
            inv.setItem(27, invite);
            inv.setItem(28, invite);

            ItemStack promote = new ItemBuilder(MatRepo.INVISIBLE).name("&7Promote Player").create();
            inv.setItem(29, promote);
            inv.setItem(30, promote);

            ItemStack kick = new ItemBuilder(MatRepo.INVISIBLE).name("&7Kick Player").create();
            inv.setItem(31, kick);
            inv.setItem(32, kick);

            ItemStack leave = new ItemBuilder(MatRepo.INVISIBLE).name("&cDisband Party").create();
            inv.setItem(34, leave);
            inv.setItem(35, leave);
        }else{
            ItemStack leave = new ItemBuilder(MatRepo.INVISIBLE).name("&7Leave Party").create();
            inv.setItem(34, leave);
            inv.setItem(35, leave);
        }

        int index = 11;
        for(Player pl: party.getAllPlayers()){
            if(pl == null)
                continue;

            ItemStack head = new ItemBuilder(Material.PLAYER_HEAD).skullOwner(pl).name(pl.getDisplayName()).create();
            inv.setItem(index, head);
            index++;
        }

        return inv;
    }
}
