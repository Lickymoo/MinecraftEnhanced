package com.buoobuoo.minecraftenhanced.core.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerStopSpectatingEntityEvent extends PlayerEvent {
    public PlayerStopSpectatingEntityEvent(Player who) {
        super(who);
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
