package com.buoobuoo.minecraftenhanced.core.event.update;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerLevelUpEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public PlayerLevelUpEvent(Player player){
        super(player);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
