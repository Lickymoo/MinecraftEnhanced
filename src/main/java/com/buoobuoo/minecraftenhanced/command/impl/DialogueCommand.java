package com.buoobuoo.minecraftenhanced.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.DialogueNextEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


@CommandAlias("diagnext")
public class DialogueCommand extends BaseCommand {

    private final MinecraftEnhanced plugin;

    public DialogueCommand(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @Default
    public void def(Player player, String id){
        DialogueNextEvent event = new DialogueNextEvent(player, id);
        Bukkit.getPluginManager().callEvent(event);
    }
}
