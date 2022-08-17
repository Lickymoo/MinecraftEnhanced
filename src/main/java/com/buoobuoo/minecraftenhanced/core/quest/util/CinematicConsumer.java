package com.buoobuoo.minecraftenhanced.core.quest.util;

import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicSequence;
import org.bukkit.entity.Player;

public interface CinematicConsumer {
    CinematicSequence accept(Player player);
}
