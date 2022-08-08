package com.buoobuoo.minecraftenhanced.persistence.serialization.impl;

import com.buoobuoo.minecraftenhanced.core.ability.AbilityCastType;
import com.buoobuoo.minecraftenhanced.persistence.serialization.VariableSerializer;

public class AbilityCastTypeSerializer extends VariableSerializer<AbilityCastType> {

    @Override
    public String serialize(AbilityCastType obj) {
        if(obj == null)
            return "";
        return obj.name();
    }

    @Override
    public AbilityCastType deserialize(String str) {
        try {
            return AbilityCastType.valueOf(str);
        } catch (Exception ex) {
            return AbilityCastType.NONE;
        }
    }
}
