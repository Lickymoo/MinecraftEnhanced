package com.buoobuoo.minecraftenhanced.persistence.serialization.impl;

import com.buoobuoo.minecraftenhanced.core.permission.PermissionGroup;
import com.buoobuoo.minecraftenhanced.persistence.serialization.VariableSerializer;

public class PermissionGroupSerializer extends VariableSerializer<PermissionGroup> {
    @Override
    public String serialize(PermissionGroup obj) {
        return (obj).getGroupID();

    }

    @Override
    public PermissionGroup deserialize(String str) {
        return PermissionGroup.getGroup(str);
    }


}