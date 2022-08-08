package com.buoobuoo.minecraftenhanced.core.event;

import com.buoobuoo.minecraftenhanced.core.area.Area;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class AreaExitEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Area area;

    public AreaExitEvent(Player player, Area area){
        super(player);
        this.area = area;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
