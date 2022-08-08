package com.buoobuoo.minecraftenhanced.core.vfx.cinematic;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@Getter
public class CinematicFrame {
    public int delay;
    public Consumer<Player> function;

    public CinematicFrame(int delay, Consumer<Player> function) {
        this.delay = delay;
        this.function = function;
    }

    public CinematicFrame[] singleArray() {
        return new CinematicFrame[] {this};
    }
}