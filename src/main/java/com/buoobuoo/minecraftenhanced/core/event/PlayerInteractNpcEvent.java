package com.buoobuoo.minecraftenhanced.core.event;

import com.buoobuoo.minecraftenhanced.core.entity.interf.NpcEntity;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerInteractNpcEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final NpcEntity handler;

    public PlayerInteractNpcEvent(Player player, NpcEntity handler){
        super(player);
        this.handler = handler;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
