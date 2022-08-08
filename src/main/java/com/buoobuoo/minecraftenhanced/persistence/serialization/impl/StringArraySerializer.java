package com.buoobuoo.minecraftenhanced.persistence.serialization.impl;

import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.persistence.serialization.VariableSerializer;

public class StringArraySerializer extends VariableSerializer<String[]> {
    @Override
    public String serialize(String[] obj) {
        return Util.arrToString(obj);
    }

    @Override
    public String[] deserialize(String str) {
        return Util.stringToArr(str);
    }
}
