package com.buoobuoo.minecraftenhanced.persistence.serialization.impl;

import com.buoobuoo.minecraftenhanced.persistence.serialization.VariableSerializer;

import java.util.UUID;

public class UUIDSerializer extends VariableSerializer<UUID> {
    @Override
    public String serialize(UUID obj) {
        return obj.toString();
    }

    @Override
    public UUID deserialize(String str) {
        return UUID.fromString(str);
    }
}
