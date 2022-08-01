package com.buoobuoo.minecraftenhanced.core.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PartyInviteUpdateEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public PartyInviteUpdateEvent(Player player) {
        super(player);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}