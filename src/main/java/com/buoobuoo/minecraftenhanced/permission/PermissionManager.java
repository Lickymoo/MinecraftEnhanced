package com.buoobuoo.minecraftenhanced.permission;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.UUID;
/*
 * Probably should be it's own plugin
 */
public class PermissionManager {
    private final MinecraftEnhanced plugin;

    public static PermissionGroup defaultGroup = PermissionGroup.MEMBER;

    public PermissionManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    public static HashMap<UUID, PermissionAttachment> permissionList = new HashMap<>();

    public void removePermission(Player player, String perm) {
        permissionList.get(player.getUniqueId()).unsetPermission(perm);
    }

    public void setPermission(Player player, String perm) {
        setPermission(player, perm, true);
    }

    public void setPermission(Player player, String perm, boolean value) {
        if(!permissionList.containsKey(player.getUniqueId())) return;
        permissionList.get(player.getUniqueId()).setPermission(perm, value);
    }

    public void setPlayerGroup(Player player, PermissionGroup group) {
        PlayerData user = plugin.getPlayerManager().getPlayer(player);
        user.setGroup(group);
        assignPermissions(player);
    }

    /*
     * Called on join
     */
    public void assignPermissions(Player player) {
        PlayerData user = plugin.getPlayerManager().getPlayer(player);
        if(user.getGroup() == null) user.setGroup(defaultGroup);
        for(String str : user.getGroup().getAllPerms())
        {
            setPermission(player, str);
        }
    }

}


