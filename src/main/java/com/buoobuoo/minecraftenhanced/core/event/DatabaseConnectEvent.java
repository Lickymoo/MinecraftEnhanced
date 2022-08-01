package com.buoobuoo.minecraftenhanced.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DatabaseConnectEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}