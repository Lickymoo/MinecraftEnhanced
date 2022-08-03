package com.buoobuoo.minecraftenhanced.core.event;

import com.buoobuoo.minecraftenhanced.core.entity.npc.NpcInstance;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInteractNpcEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private Player player;

    @Getter
    private NpcInstance handler;

    public PlayerInteractNpcEvent(Player player, NpcInstance handler){
        this.player = player;
        this.handler = handler;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
