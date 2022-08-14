package com.buoobuoo.minecraftenhanced.core.quest;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.quest.impl.act1.ACT1_MQ1;
import com.buoobuoo.minecraftenhanced.core.quest.impl.act1.ACT1_MQ2;
import com.buoobuoo.minecraftenhanced.core.quest.impl.act1.ACT1_MQ3;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QuestManager {
    private List<QuestLine> registeredQuests = new ArrayList<>();

    private final MinecraftEnhanced plugin;

    public QuestManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    public void init(){
        registerQuests(
                new ACT1_MQ1(plugin),
                new ACT1_MQ2(plugin),
                new ACT1_MQ3(plugin)
        );
    }

    public void registerQuests(QuestLine... quests){
        registeredQuests.addAll(List.of(quests));
        plugin.registerEvents(quests);
    }

    public QuestLine getQuestByClass(Class<? extends QuestLine> clazz){
        for(QuestLine questLine : registeredQuests){
            if(questLine.getClass() == clazz)
                return questLine;
        }
        return null;
    }

    public QuestLine getQuestByID(String id){
        for(QuestLine questLine : registeredQuests){
            if(questLine.getQuestID().equalsIgnoreCase(id))
                return questLine;
        }
        return null;
    }

    public Class<? extends QuestLine> getQuestClassByID(String id){
        for(QuestLine questLine : registeredQuests){
            if(questLine.getQuestID().equalsIgnoreCase(id))
                return questLine.getClass();
        }
        return null;
    }

    public List<String> allQuestID(){
        List<String> quests = new ArrayList<>();
        for(QuestLine quest : registeredQuests){
            quests.add(quest.getQuestID());
        }
        return quests;
    }

    public void startQuest(String questID, ProfileData profileData){
        startQuest(getQuestClassByID(questID), profileData);
    }

    public void startQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        Player player = Bukkit.getPlayer(profileData.getOwnerID());
        questLine.start(player);
    }

    public void finishQuest(String questID, ProfileData profileData){
        finishQuest(getQuestClassByID(questID), profileData);
    }

    public void finishQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        applyCompletedToProfile(questClass, profileData);
    }

    public void resetQuest(String questID, ProfileData profileData){
        resetQuest(getQuestClassByID(questID), profileData);
    }

    public void resetQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        removeActiveQuest(questClass, profileData);
        profileData.getCompletedQuest().remove(questLine.getQuestID());
    }

    public void loadQuests(ProfileData profileData){
        for(String str : profileData.getActiveQuests()){
            String questID = str.split(":")[0];
            QuestLine questLine = getQuestByID(questID);
            Player player = Bukkit.getPlayer(profileData.getOwnerID());

            questLine.loadQuestString(player, str);
        }
    }

    public void applyActiveToProfile(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        removeActiveQuest(questClass, profileData);
        String str = questLine.saveQuestString(player);
        profileData.getActiveQuests().add(str);
    }

    public void applyCompletedToProfile(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        removeActiveQuest(questClass, profileData);
        profileData.getCompletedQuest().add(questLine.getQuestID());
    }

    public void removeActiveQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        String flag = null;
        for(String str : profileData.getActiveQuests()){
            String questID = str.split(":")[0];
            if(questLine.getQuestID().equalsIgnoreCase(questID))
                flag = str;
        }
        if(flag != null)
            profileData.getActiveQuests().remove(flag);
    }

    public boolean hasActiveQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        for(String str : profileData.getActiveQuests()){
            String questID = str.split(":")[0];
            if(questLine.getQuestID().equalsIgnoreCase(questID))
                return true;
        }
        return false;
    }

    public boolean hasCompletedQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        for(String str : profileData.getCompletedQuest()){
            if(questLine.getQuestID().equalsIgnoreCase(str))
                return true;
        }
        return false;
    }

    public void clearPlayer(Player player){
        for(QuestLine questLine : registeredQuests){
            questLine.clearPlayer(player);
        }
    }
}







































