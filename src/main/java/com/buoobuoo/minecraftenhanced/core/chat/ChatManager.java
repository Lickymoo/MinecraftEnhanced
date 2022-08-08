package com.buoobuoo.minecraftenhanced.core.chat;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.party.Party;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener {

    private final MinecraftEnhanced plugin;

    public ChatManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        String message = event.getMessage();

        //color coding
        message = Util.formatColour(message);
        event.setMessage(message);

        String prefix = message.split(" ")[0];
        ChatContext context = ChatContext.matchContext(prefix);
        Player player = event.getPlayer();
        PlayerData senderData = plugin.getPlayerManager().getPlayer(player);

        if(context != ChatContext.GLOBAL) {
            event.setMessage(message.replaceFirst(prefix, ""));
            switch (context){
                case PARTY:
                    Party party = plugin.getPartyManager().getPartyByPlayer(player);
                    if(party == null){
                        player.sendMessage(Util.formatColour("&7You are not in a party"));
                    }else{
                        party.sendMessage("&d@party &f" + player.getDisplayName() + ":&7" + event.getMessage());
                    }
                    break;
                case STAFF:
                    if(!senderData.getGroup().hasPermission("me.admin")){
                        player.sendMessage(Util.formatColour("&7You are not a staff member"));
                        break;
                    }
                    for(Player pl : Bukkit.getOnlinePlayers()){
                        PlayerData playerData = plugin.getPlayerManager().getPlayer(pl);
                        if(playerData.getGroup().hasPermission("me.admin")){
                            pl.sendMessage(Util.formatColour("&b@staff &f" + player.getDisplayName() + ":&7" + event.getMessage()));
                        }
                    }
                    break;
                default:
                    break;
            }

            event.setCancelled(true);
        }

    }
}
