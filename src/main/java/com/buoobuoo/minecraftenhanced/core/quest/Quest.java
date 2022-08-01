package com.buoobuoo.minecraftenhanced.core.quest;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

@Getter
public abstract class Quest implements Listener {
    protected final QuestManager questManager;

    private String questName;
    private String questID;
    private String questBrief;

    public Quest(QuestManager questManager, String questID, String questName, String questBrief){
        this.questManager = questManager;

        this.questName = questName;
        this.questID = questID;
        this.questBrief = questBrief;
    }

    public int getProgress(Player player){
        return questManager.getProgress(player, this);
    }

    public boolean isApplicable(Player player){
        return questManager.playerHasStartedQuest(player, this);
    }

    public void finishQuest(Player player){
        if(!questManager.playerHasStartedQuest(player, this))
            return;

        questManager.completeQuest(player, this);
    }

    public abstract void onComplete(Player player);
}

