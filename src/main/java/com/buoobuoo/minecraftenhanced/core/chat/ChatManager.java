package com.buoobuoo.minecraftenhanced.core.chat;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.social.party.Party;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class ChatManager implements Listener {

    public static final String OVERRIDE_TAG = "@$OR$@";
    private static final String CLEAR_STRING_TEXT = StringUtils.repeat(OVERRIDE_TAG + " \n", 100);
    private final MinecraftEnhanced plugin;
    private static final Map<UUID, ChatRestrictionMode> chatMode = new HashMap<>();
    private static final Map<UUID, LinkedList<BaseComponent[]>> previousChat = new HashMap<>();

    public ChatManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    public void receiveMessage(Player player, BaseComponent[] baseComponents){
        UUID uuid = player.getUniqueId();
        previousChat.putIfAbsent(uuid, new LinkedList<>());
        previousChat.get(uuid).add(baseComponents);
        if(previousChat.get(uuid).size() > 100){
            previousChat.get(uuid).pop();
        }
    }

    public ChatRestrictionMode getChatMode(Player player){
        return chatMode.getOrDefault(player.getUniqueId(), ChatRestrictionMode.UNRESTRICTED);
    }

    public void setChatMode(Player player, ChatRestrictionMode mode){
        UUID uuid = player.getUniqueId();
        List<BaseComponent[]> chats = previousChat.get(uuid);

        chatMode.put(player.getUniqueId(), mode);
        switch (mode){
            case UNRESTRICTED -> {
                player.sendMessage(OVERRIDE_TAG + CLEAR_STRING_TEXT);
                for(BaseComponent[] components : previousChat.getOrDefault(uuid, new LinkedList<>())){
                    BaseComponent[] sendComponents = Util.concatenateArrays(TextComponent.fromLegacyText(OVERRIDE_TAG), components);

                    player.spigot().sendMessage(sendComponents);
                }
            }
            case DIALOGUE_RESTRICTION -> {
                player.sendMessage(OVERRIDE_TAG + CLEAR_STRING_TEXT);

                for(BaseComponent[] components : chats){
                    BaseComponent[] newComponents = new BaseComponent[components.length];

                    //grayscale
                    for(int i = 0; i < components.length; i++){
                        TextComponent component = new TextComponent(components[i].toLegacyText());
                        String text = component.getText();
                        text = Util.grayScaleString(text);
                        component.setText(text);
                        newComponents[i] = component;
                    }

                    BaseComponent[] sendComponents = Util.concatenateArrays(TextComponent.fromLegacyText(OVERRIDE_TAG), newComponents);
                    player.spigot().sendMessage(sendComponents);
                }
            }
        }
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

        event.setCancelled(true);

        if(getChatMode(player) != ChatRestrictionMode.UNRESTRICTED){
            return;
        }

        switch (context){
            case PARTY:
                event.setMessage(message.replaceFirst(prefix, ""));
                Party party = plugin.getPartyManager().getPartyByPlayer(player);
                if(party == null){
                    player.sendMessage(Util.formatColour("&7You are not in a party"));
                }else{
                    party.sendMessage("&d@party &f" + player.getDisplayName() + ":&7" + event.getMessage());
                }
                break;
            case STAFF:
                event.setMessage(message.replaceFirst(prefix, ""));
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
            case GLOBAL:
                //replace message with system chat makes life easier
                Component component = Component.literal(Util.formatColour("&f" + player.getDisplayName() + ":&7 " + event.getMessage()));

                ClientboundSystemChatPacket packet = new ClientboundSystemChatPacket(component, 1);
                Util.sendPacketGlobal(packet);
                break;
            default:
                break;
        }

    }


}
