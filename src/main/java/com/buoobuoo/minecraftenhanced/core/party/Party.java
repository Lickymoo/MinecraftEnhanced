package com.buoobuoo.minecraftenhanced.core.party;

import com.buoobuoo.minecraftenhanced.core.util.Util;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Party {
    private final PartyManager partyManager;

    public static int maxPartySize = 5;

    private UUID partyID;
    private UUID leader;
    private List<UUID> members;
    private List<UUID> invites;
    private boolean isDisbanded;

    public Party(PartyManager partyManager, Player leader){
        this.partyManager = partyManager;

        this.leader = leader.getUniqueId();
        this.members = new ArrayList<>();
        this.invites = new ArrayList<>();

        this.partyID = UUID.randomUUID();
    }

    public boolean hasPlayer(Player player){
        UUID uuid = player.getUniqueId();

        if(leader == uuid)
            return true;

        if(members.contains(uuid))
            return true;

        return false;
    }

    public void addPlayer(Player player){
        this.members.add(player.getUniqueId());
    }

    public void removePlayer(Player player){
        this.members.remove(player.getUniqueId());
    }

    public boolean promotePlayer(Player player){
        if(members.contains(player.getUniqueId())){
            members.add(leader);
            members.remove(player.getUniqueId());
            leader = player.getUniqueId();
            return true;
        }else{
            return false;
        }
    }

    public boolean isLeader(Player player){
        return leader == player.getUniqueId();
    }

    public void acceptInvite(Player player){
        removeInvite(player);
        addPlayer(player);
    }

    public void invite(Player player){
        invites.add(player.getUniqueId());
    }

    public void removeInvite(Player player){
        invites.remove(player.getUniqueId());
    }

    public boolean isInvited(Player player){
        return invites.contains(player.getUniqueId());
    }

    public void sendMessage(String str){
        for(Player player : getAllPlayers()){
            player.sendMessage(Util.formatColour(str));
        }
    }

    public void disband(){
        this.isDisbanded = true;
        this.members.clear();
        this.leader = null;

        this.partyManager.removeParty(this);
    }

    public List<Player> getAllPlayers(){
        List<Player> players = new ArrayList<>();
        players.add(Bukkit.getPlayer(leader));

        for(UUID uuid : members){
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) {
                continue;
            }
            players.add(player);
        }
        return players;
    }

    public List<Player> getAllMembers(){
        List<Player> players = new ArrayList<>();
        for(UUID uuid : members){
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) {
                continue;
            }
            players.add(player);
        }
        return players;
    }
}
