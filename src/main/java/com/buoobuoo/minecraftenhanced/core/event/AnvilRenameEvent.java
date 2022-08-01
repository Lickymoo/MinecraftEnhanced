package com.buoobuoo.minecraftenhanced.core.event;

import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class AnvilRenameEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private CustomInventory handler;

    @Getter
    private String name;

    public AnvilRenameEvent(Player player, CustomInventory handler, String name){
        super(player);
        this.handler = handler;
        this.name = name;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
