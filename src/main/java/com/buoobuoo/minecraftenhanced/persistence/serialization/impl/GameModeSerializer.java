package com.buoobuoo.minecraftenhanced.persistence.serialization.impl;

import com.buoobuoo.minecraftenhanced.persistence.serialization.VariableSerializer;
import org.bukkit.GameMode;

import java.util.UUID;

public class GameModeSerializer extends VariableSerializer<GameMode> {

    @Override
    public String serialize(GameMode obj) {
        return obj.name();
    }

    @Override
    public GameMode deserialize(String str) {
        return GameMode.valueOf(str);
    }
}
