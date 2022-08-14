package com.buoobuoo.minecraftenhanced.command.impl;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.impl.quest.QuestMainInventory;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandAlias("quest|quests")
public class QuestCommand extends BaseCommand {

    private final MinecraftEnhanced plugin;
    private final QuestManager questManager;

    public QuestCommand(MinecraftEnhanced plugin) {
        this.plugin = plugin;
        this.questManager = plugin.getQuestManager();
    }

    @Default
    public void quest(Player player){
        Inventory inv = new QuestMainInventory(plugin ,player).getInventory();
        player.openInventory(inv);
    }

    @Subcommand("start")
    @CommandCompletion("@players @quests")
    public void start(Player player, OnlinePlayer target, String questID){
        ProfileData profileData = plugin.getPlayerManager().getProfile(target.getPlayer());
        questManager.startQuest(questID, profileData);
    }

    @Subcommand("finish")
    @CommandCompletion("@players @quests")
    public void finish(Player player, OnlinePlayer target, String questID){
        //Force start
        ProfileData profileData = plugin.getPlayerManager().getProfile(target.getPlayer());
        questManager.finishQuest(questID, profileData);
    }

    @Subcommand("reset")
    @CommandCompletion("@players @quests")
    public void reset(Player player, OnlinePlayer target, String questID){
        ProfileData profileData = plugin.getPlayerManager().getProfile(target.getPlayer());
        questManager.resetQuest(questID, profileData);
    }


}