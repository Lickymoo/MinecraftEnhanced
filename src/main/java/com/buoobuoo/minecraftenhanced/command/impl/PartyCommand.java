package com.buoobuoo.minecraftenhanced.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.impl.EmptyPartyInventory;
import com.buoobuoo.minecraftenhanced.core.inventory.impl.PartyInventory;
import com.buoobuoo.minecraftenhanced.core.party.Party;
import com.buoobuoo.minecraftenhanced.core.party.PartyManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandAlias("party|p")
public class PartyCommand extends BaseCommand {

    private final MinecraftEnhanced plugin;
    private final PartyManager partyManager;

    public PartyCommand(MinecraftEnhanced plugin){
        this.plugin = plugin;
        this.partyManager = plugin.getPartyManager();
    }

    @Default
    public void main(Player player){
        boolean hasParty = partyManager.isInParty(player);
        if(!hasParty){
            Inventory inv = new EmptyPartyInventory(plugin, player).getInventory();
            player.openInventory(inv);
            return;
        }

        Party party = partyManager.getPartyByPlayer(player);
        Inventory inv = new PartyInventory(plugin, player, party).getInventory();
        player.openInventory(inv);
    }

    @Subcommand("create")
    public void create(Player player){
        partyManager.create(player);

    }

    @Subcommand("disband")
    public void disband(Player player){
        partyManager.disband(player);
    }

    @Subcommand("leave")
    public void leave(Player player){
        partyManager.leave(player);
    }

    @Subcommand("join")
    public void join(Player player, OnlinePlayer target){
        partyManager.join(player, target.getPlayer());
    }

    @Subcommand("kick")
    public void kick(Player player, OnlinePlayer target){
        partyManager.kick(player, target.getPlayer());
    }

    @Subcommand("invite")
    public void invite(Player player, OnlinePlayer target){
        partyManager.invite(player, target.getPlayer());
    }

    @Subcommand("promote")
    public void promote(Player player, OnlinePlayer target){
        partyManager.promote(player, target.getPlayer());
    }
}
