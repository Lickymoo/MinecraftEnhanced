package com.buoobuoo.minecraftenhanced.core.event.update;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdateTickEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}