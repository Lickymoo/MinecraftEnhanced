package com.buoobuoo.minecraftenhanced.core.quest;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.quest.impl.IntroQuest;
import com.buoobuoo.minecraftenhanced.core.util.Pair;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QuestManager {
    private final MinecraftEnhanced plugin;

    private final List<Quest> questList = new ArrayList<>();

    public QuestManager(MinecraftEnhanced plugin){
        this.plugin = plugin;

        //Quests
        registerQuests(
            new IntroQuest(this)
        );
    }

    public boolean beginQuest(Player player, Quest quest){
        if(quest == null)
            return false;

        String questID = quest.getQuestID();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        if(playerHasCompletedQuest(player, quest) || playerHasStartedQuest(player, quest))
            return false;

        Util.sendDialogueBox(player, CharRepo.UI_PORTRAIT_BOUNCER_TUFF, quest.getQuestName(), quest.getQuestBrief());

        profileData.getActiveQuests().add(activeQuestToString(questID, 0));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1 ,1);
        return true;
    }

    public void completeQuest(Player player, Quest quest){
        if(quest == null)
            return;

        String questID = quest.getQuestID();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        profileData.getActiveQuests().remove(getActiveQuestString(player, questID));
        profileData.getCompletedQuest().add(questID);
        quest.onComplete(player);
    }

    public void resetQuest(Player player, Quest quest){
        if(quest == null)
            return;

        String questID = quest.getQuestID();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        profileData.getActiveQuests().remove(getActiveQuestString(player, questID));
        profileData.getCompletedQuest().remove(questID);
    }

    public Quest getQuestByID(String questID){
        questID = questID.toUpperCase();
        for(Quest quest : questList){
            if(quest.getQuestID().equalsIgnoreCase(questID))
                return quest;
        }
        return null;
    }

    public void registerQuests(Quest... quests) {
        for (Quest quest : quests) {
            questList.add(quest);
            plugin.registerEvents(quest);
        }
    }
    public boolean playerHasCompletedQuest(Player player, Quest quest){
        if(quest == null)
            return false;

        String questID = quest.getQuestID();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        for(String str : profileData.getCompletedQuest()){
            if(str.equalsIgnoreCase(questID))
                return true;
        }
        return false;
    }

    public boolean playerHasStartedQuest(Player player, Quest quest){
        if(quest == null)
            return false;
        String questID = quest.getQuestID();
        Pair<String, Integer> progress = parseActiveQuest(player, questID);

        return progress != null;
    }

    public int getProgress(Player player, Quest quest){
        if(quest == null)
            return -1;

        String questID = quest.getQuestID();
        Pair<String, Integer> progress = parseActiveQuest(player, questID);
        return progress == null ? -1 : progress.getRight();
    }

    public List<String> allQuestID(){
        List<String> quests = new ArrayList<>();
        for(Quest quest : questList){
            quests.add(quest.getQuestID());
        }
        return quests;
    }

    public Pair<String, Integer> parseActiveQuest(Player player, String questID){
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        for(String s : profileData.getActiveQuests()){
            if(s.contains(questID)){
                String[] str = s.split(":");
                return Pair.of(str[0], Integer.parseInt(str[1]));
            }
        }
        return null;
    }

    public String getActiveQuestString(Player player, String questID){
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        for(String str : profileData.getActiveQuests()){
            if(str.contains(questID)) {
                return str;
            }
        }
        return null;
    }

    public String activeQuestToString(String questID, int val){
        return questID + ":" + val;
    }
}
