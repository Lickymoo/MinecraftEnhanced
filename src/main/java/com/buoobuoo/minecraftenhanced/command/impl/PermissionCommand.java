package com.buoobuoo.minecraftenhanced.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.permission.PermissionGroup;
import com.buoobuoo.minecraftenhanced.permission.PermissionManager;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import org.bukkit.command.CommandSender;

@CommandAlias("permission|perm")
public class PermissionCommand extends BaseCommand {

    private final MinecraftEnhanced plugin;
    private final PermissionManager permissionManager;

    public PermissionCommand(MinecraftEnhanced plugin){
        this.plugin = plugin;
        this.permissionManager = plugin.getPermissionManager();
    }


    @HelpCommand
    public void help(final CommandSender sender) {

    }

    @Subcommand("group")
    public class group extends BaseCommand{

        @Subcommand("set")
        @CommandCompletion("@players @perm-groups")
        public void set(final CommandSender sender, OnlinePlayer target, PermissionGroup group)
        {
            permissionManager.setPlayerGroup(target.player, group);
            sender.sendMessage(Util.formatColour(target.player.getDisplayName() + "&7's group set to " + plugin.getPlayerManager().getPlayer(target.player).getGroup().getGroupID()));

        }
    }


}




