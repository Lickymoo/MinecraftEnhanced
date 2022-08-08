package com.buoobuoo.minecraftenhanced.persistence.serialization.impl;

import com.buoobuoo.minecraftenhanced.core.ability.AbilityCastType;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.persistence.serialization.VariableSerializer;

public class AbilityCastTypeArraySerializer extends VariableSerializer<AbilityCastType[]> {

    @Override
    public String serialize(AbilityCastType[] obj) {
        AbilityCastTypeSerializer singularSerializer = new AbilityCastTypeSerializer();
        String[] types = new String[obj.length];

        for(int i = 0; i < obj.length; i++){
            String str = singularSerializer.serialize(obj[i]);
            types[i] = str;
        }
        return Util.arrToString(types);

    }

    @Override
    public AbilityCastType[] deserialize(String str) {
        AbilityCastTypeSerializer singularSerializer = new AbilityCastTypeSerializer();
        String[] types = Util.stringToArr(str);

        AbilityCastType[] abs = new AbilityCastType[types.length];

        for(int i = 0; i < types.length; i++){
            abs[i] = singularSerializer.deserialize(types[i]);
        }

        return abs;
    }
}
