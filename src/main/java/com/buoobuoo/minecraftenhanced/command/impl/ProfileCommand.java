package com.buoobuoo.minecraftenhanced.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.impl.profile.ProfileInventory;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


@CommandAlias("profile|prof|profiles")
public class ProfileCommand extends BaseCommand {

    private final MinecraftEnhanced plugin;

    public ProfileCommand(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @Default
    public void def(Player player){
        Inventory inv = new ProfileInventory(plugin, player).getInventory();
        PlayerData playerData = plugin.getPlayerManager().getPlayer(player);


        playerData.setCurrentProfile(null);
        player.openInventory(inv);
    }
}
