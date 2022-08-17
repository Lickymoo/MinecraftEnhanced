package com.buoobuoo.minecraftenhanced.core.social.friends;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.Util;

import java.util.ArrayList;

public class FriendsManager {
    private final MinecraftEnhanced plugin;

    public FriendsManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    private void checkPlayer(PlayerData playerData){
        if(playerData.getFriends() == null)
            playerData.setFriends(new ArrayList<>());
        if(playerData.getFriendRequestsReceived() == null)
            playerData.setFriendRequestsReceived(new ArrayList<>());
    }

    public void sendFriendRequest(PlayerData sender, PlayerData target){
        checkPlayer(sender);
        checkPlayer(target);
        if(sender == target){
            sender.getPlayer().sendMessage(Util.formatColour("&7You cannot befriend yourself"));
            return;
        }
        if(areFriends(sender, target)){
            sender.getPlayer().sendMessage(Util.formatColour("&7You are already friends with &f" + target.getPlayer().getName() + "&7."));
            return;
        }
        if(hasFriendRequest(target, sender)){
            sender.getPlayer().sendMessage(Util.formatColour("&7You have already send a friend request to &f" + target.getPlayer().getName() + "&7."));
            return;
        }

        target.getFriendRequestsReceived().add(sender.getOwnerID());
        sender.getPlayer().sendMessage(Util.formatColour("&7Friend request sent to &f" + target.getPlayer().getName() + "&7."));
        target.getPlayer().sendMessage(Util.formatColour("&7You have received a friend request from &f" + sender.getPlayer().getName() + "&7."));
        target.getPlayer().sendMessage(Util.formatColour("&7Enter &f/friend add" + sender.getPlayer().getName() + " &7to accept."));
    }

    public void acceptFriendRequest(PlayerData a, PlayerData b){
        checkPlayer(a);
        checkPlayer(b);
        a.getFriendRequestsReceived().remove(b.getOwnerID());
        addFriend(a, b);
        a.getPlayer().sendMessage(Util.formatColour("&7Accepted &f" + b.getPlayer().getName() + "'s &7friend request."));
        b.getPlayer().sendMessage(Util.formatColour("&f" + a.getPlayer().getName() + " &7has accepted your friend request."));
    }

    public void addFriend(PlayerData a, PlayerData b){
        checkPlayer(a);
        checkPlayer(b);
        a.getFriends().add(b.getOwnerID());
        b.getFriends().add(a.getOwnerID());
    }

    public void removeFriend(PlayerData a, PlayerData b){
        checkPlayer(a);
        checkPlayer(b);
        if(a == b){
            a.getPlayer().sendMessage(Util.formatColour("&7You cannot unfriend yourself"));
            return;
        }
        if(!areFriends(a, b)){
            a.getPlayer().sendMessage(Util.formatColour("&7You are not friends with &f" + b.getPlayer().getName() + "&7."));
            return;
        }

        a.getFriends().remove(b.getOwnerID());
        b.getFriends().remove(a.getOwnerID());

        a.getPlayer().sendMessage(Util.formatColour("&7Removed friend &f" + b.getPlayer().getName() + "&7."));
    }

    public boolean hasFriendRequest(PlayerData a, PlayerData b){
        checkPlayer(a);
        checkPlayer(b);
        return a.getFriendRequestsReceived().contains(b.getOwnerID());
    }

    public boolean areFriends(PlayerData a, PlayerData b){
        return a.getFriends().contains(b.getOwnerID());
    }
}
