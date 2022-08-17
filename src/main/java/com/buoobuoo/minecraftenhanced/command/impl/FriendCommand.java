package com.buoobuoo.minecraftenhanced.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import com.buoobuoo.minecraftenhanced.core.social.friends.FriendsManager;
import com.buoobuoo.minecraftenhanced.core.social.party.PartyManager;
import org.bukkit.entity.Player;


@CommandAlias("friend|friends|friendship|friendships")
public class FriendCommand extends BaseCommand {
    private final MinecraftEnhanced plugin;

    public FriendCommand(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @Subcommand("add")
    public void add(Player player, OnlinePlayer target){
        FriendsManager friendsManager = plugin.getFriendsManager();
        PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
        PlayerData targetData = plugin.getPlayerManager().getPlayer(target.getPlayer());
        if(friendsManager.hasFriendRequest(playerData, targetData)){
            friendsManager.acceptFriendRequest(playerData, targetData);
            return;
        }
        friendsManager.sendFriendRequest(playerData, targetData);
    }

    @Subcommand("remove|rm|rmv")
    public void remove(Player player, OnlinePlayer target){
        FriendsManager friendsManager = plugin.getFriendsManager();
        PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
        PlayerData targetData = plugin.getPlayerManager().getPlayer(target.getPlayer());
        friendsManager.removeFriend(playerData, targetData);
    }
}
