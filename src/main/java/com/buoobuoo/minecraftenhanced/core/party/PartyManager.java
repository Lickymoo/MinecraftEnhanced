package com.buoobuoo.minecraftenhanced.core.party;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.PartyInviteUpdateEvent;
import com.buoobuoo.minecraftenhanced.core.event.PartyUpdateEvent;
import com.buoobuoo.minecraftenhanced.core.util.JSONUtil;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PartyManager {
    private final MinecraftEnhanced plugin;

    private List<Party> partyList = new ArrayList<>();

    public PartyManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    public Party getPartyByPlayer(Player player){
        for(Party party : partyList){
            if(party.hasPlayer(player))
                return party;
        }
        return null;
    }

    public void registerParty(Party party){
        this.partyList.add(party);
    }

    public void removeParty(Party party){
        this.partyList.remove(party);
    }

    public boolean isInParty(Player player){
        Party party = getPartyByPlayer(player);
        return party != null;
    }

    public boolean isLeader(Player player){
        Party party = getPartyByPlayer(player);
        return party.isLeader(player);
    }

    public List<Party> getInvitedParties(Player player){
        List<Party> parties = new ArrayList<>();
        for(Party party : partyList){
            if(party.isInvited(player))
                parties.add(party);
        }
        return parties;
    }

    public void create(Player player){
        boolean hasParty = this.isInParty(player);
        if(hasParty){
            player.sendMessage(Util.formatColour("&7You are already in a party"));
            return;
        }
        player.sendMessage(Util.formatColour("&7Party created"));

        Party party = new Party(this, player);
        this.registerParty(party);

    }
    
    public void disband(Player player){
        boolean hasParty = this.isInParty(player);
        if(!hasParty){
            player.sendMessage(Util.formatColour("&7You are not in a party"));
            return;
        }
        if(!this.isLeader(player)){
            player.sendMessage(Util.formatColour("&7You are not the leader of this party"));
            return;
        }

        Party party = this.getPartyByPlayer(player);
        party.sendMessage(Util.formatColour("&7The party has been disbanded"));

        party.disband();

        Bukkit.getServer().getPluginManager().callEvent(new PartyUpdateEvent(party));
    }

    public void leave(Player player){
        boolean hasParty = this.isInParty(player);
        Party party = this.getPartyByPlayer(player);
        if(!hasParty){
            player.sendMessage(Util.formatColour("&7You are not in a party"));
            return;
        }
        if(this.isLeader(player)){
            player.sendMessage(Util.formatColour("&7You cannot leave as the leader. Use &f/party disband"));
            return;
        }

        party.removePlayer(player);
        party.sendMessage(player.getDisplayName() + " &7has left the party");
        Bukkit.getServer().getPluginManager().callEvent(new PartyUpdateEvent(party));
    }

    public void join(Player player, Player target){
        Player targetPlayer = target.getPlayer();
        boolean hasParty = this.isInParty(player);
        if(hasParty){
            player.sendMessage(Util.formatColour("&7You are already in a party"));
            return;
        }
        Party targetParty = this.getPartyByPlayer(targetPlayer);
        //target
        if(targetParty == null){
            player.sendMessage(Util.formatColour("&7Player is not in a party"));
            return;
        }
        if(!targetParty.isInvited(player)){
            player.sendMessage(Util.formatColour("&7You are not invited to this party"));
            return;
        }
        targetParty.acceptInvite(player);

        targetParty.sendMessage(player.getDisplayName() + " &7has joined the party");
        Bukkit.getServer().getPluginManager().callEvent(new PartyUpdateEvent(targetParty));

    }

    public void kick(Player player, Player target){
        Player targetPlayer = target.getPlayer();
        boolean hasParty = this.isInParty(player);
        if(!hasParty){
            player.sendMessage(Util.formatColour("&7You are not in a party"));
            return;
        }
        if(!this.isLeader(player)){
            player.sendMessage(Util.formatColour("&7You are not the leader of this party"));
            return;
        }
        Party party = this.getPartyByPlayer(player);
        //target
        if(!party.hasPlayer(targetPlayer)){
            player.sendMessage(Util.formatColour("&7Player is not in this party"));
            return;
        }
        if(party.isLeader(targetPlayer)){
            player.sendMessage(Util.formatColour("&7You cannot kick this player"));
            return;
        }

        party.sendMessage(targetPlayer.getDisplayName() + " &7has been kicked from the party");
        targetPlayer.sendMessage(Util.formatColour("&7You have been kicked from the party"));
        Bukkit.getServer().getPluginManager().callEvent(new PartyUpdateEvent(party));
        party.removePlayer(targetPlayer);
    }

    public void invite(Player player, Player target){
        Player targetPlayer = target.getPlayer();
        boolean hasParty = this.isInParty(player);
        if(!hasParty){
            player.sendMessage(Util.formatColour("&7You are not in a party"));
            return;
        }
        if(!this.isLeader(player)){
            player.sendMessage(Util.formatColour("&7You are not the leader of this party"));
            return;
        }

        Party party = this.getPartyByPlayer(player);
        //target
        if(this.isInParty(targetPlayer)){
            player.sendMessage(Util.formatColour("&7Player is already in a party"));
            return;
        }
        if(party.isInvited(targetPlayer)){
            player.sendMessage(Util.formatColour("&7Player is already invited"));
            return;
        }

        party.invite(targetPlayer);

        party.sendMessage(targetPlayer.getDisplayName() + " &7has been invited to the party");
        targetPlayer.sendMessage(Util.formatColour(player.getDisplayName() + " &7has invited you to their party. "));

        TextComponent regular = new TextComponent(Util.formatColour(player.getDisplayName() + " &7has invited you to their party. "));
        TextComponent click = JSONUtil.getJSON(Util.formatColour("&a[Click to join party]"), "party join " + player.getName(), true, "Click to join " + player.getName() + "'s party");

        targetPlayer.spigot().sendMessage(regular, click);

        Bukkit.getServer().getPluginManager().callEvent(new PartyInviteUpdateEvent(targetPlayer));
    }

    public void promote(Player player, Player target){
        Player targetPlayer = target.getPlayer();
        boolean hasParty = this.isInParty(player);
        if(!hasParty){
            player.sendMessage(Util.formatColour("&7You are not in a party"));
            return;
        }
        if(!this.isLeader(player)){
            player.sendMessage(Util.formatColour("&7You are not the leader of this party"));
            return;
        }

        Party party = this.getPartyByPlayer(player);
        //target
        if(!party.hasPlayer(targetPlayer)){
            player.sendMessage(Util.formatColour("&7Player is not in this party"));
            return;
        }

        party.promotePlayer(targetPlayer);
        party.sendMessage(targetPlayer.getDisplayName() + " &7has been promoted");
        Bukkit.getServer().getPluginManager().callEvent(new PartyUpdateEvent(party));
    }
}
