package com.buoobuoo.minecraftenhanced.core.event.party;

import com.buoobuoo.minecraftenhanced.core.social.party.Party;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartyUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private Party party;

    public PartyUpdateEvent(Party party) {
        this.party = party;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}