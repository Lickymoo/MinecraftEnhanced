package com.buoobuoo.minecraftenhanced.core.event.questrelated;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ACT1_MQ2PickItemEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public ACT1_MQ2PickItemEvent(Player player){
        super(player);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
