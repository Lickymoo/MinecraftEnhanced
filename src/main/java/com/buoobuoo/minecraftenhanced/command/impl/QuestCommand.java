package com.buoobuoo.minecraftenhanced.command.impl;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import org.bukkit.entity.Player;

@CommandAlias("quest|quests")
public class QuestCommand extends BaseCommand {

    private final MinecraftEnhanced plugin;
    private final QuestManager questManager;

    public QuestCommand(MinecraftEnhanced plugin) {
        this.plugin = plugin;
        this.questManager = plugin.getQuestManager();
    }

    @Subcommand("start")
    @CommandCompletion("@players @quests")
    public void start(Player player, OnlinePlayer target, String questID){
        questManager.beginQuest(target.getPlayer(), questManager.getQuestByID(questID));
    }

    @Subcommand("finish")
    @CommandCompletion("@players @quests")
    public void finish(Player player, OnlinePlayer target, String questID){
        //Force start
        questManager.completeQuest(target.getPlayer(), questManager.getQuestByID(questID));
    }

    @Subcommand("reset")
    @CommandCompletion("@players @quests")
    public void reset(Player player, OnlinePlayer target, String questID){
        questManager.resetQuest(target.getPlayer(), questManager.getQuestByID(questID));
    }


}