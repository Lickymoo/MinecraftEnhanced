package com.buoobuoo.minecraftenhanced.core.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class DialogueNextEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final String id;

    public DialogueNextEvent(Player player, String id){
        super(player);
        this.id = id;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
