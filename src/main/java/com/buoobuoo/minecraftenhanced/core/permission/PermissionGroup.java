package com.buoobuoo.minecraftenhanced.core.permission;


import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PermissionGroup{

    ADMIN("ADMIN", CharRepo.RANK_ADMIN_TAG, null, "*", "clustercore.admin"),
    DEVELOPER("DEV", CharRepo.RANK_DEVELOPER_TAG, ADMIN),
    MEMBER("MEMBER", "" , null);


    PermissionGroup(String groupID, CharRepo groupPrefix, PermissionGroup inheritance, String... permissions) {
        this(groupID, groupPrefix.getCh(), inheritance, permissions);
    }


    PermissionGroup(String groupID, String groupPrefix, PermissionGroup inheritance, String... permissions)
    {
        this.groupID = groupID;
        this.groupPrefix = groupPrefix;
        this.permissions = Arrays.asList(permissions);
        this.inheritance = inheritance;
    }

    @Getter @Setter private String groupID;
    @Getter @Setter private String groupPrefix;
    @Getter @Setter private List<String> permissions;
    @Getter @Setter private PermissionGroup inheritance;

    public static PermissionGroup getGroup(String ID) {
        for(PermissionGroup g : PermissionGroup.values())
        {
            if(g.groupID.equalsIgnoreCase(ID))
                return g;
        }
        return null;
    }

    public List<String> getAllPerms(){
        if(inheritance == null) {
            return permissions;
        }else{
            List<String> perms = new ArrayList<>();
            perms.addAll(permissions);
            perms.addAll(inheritance.getAllPerms());
            return perms;
        }
    }
}