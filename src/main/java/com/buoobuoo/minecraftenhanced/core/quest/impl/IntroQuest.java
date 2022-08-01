package com.buoobuoo.minecraftenhanced.core.quest.impl;

import com.buoobuoo.minecraftenhanced.core.quest.Quest;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class IntroQuest extends Quest {
    public IntroQuest(QuestManager questManager) {
        super(questManager, "INTRO_QUEST", "Intro Quest", "I am testing stuff");
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent event){
        if(!isApplicable(event.getPlayer()))
            return;
        if(event.getBlock().getType() != Material.DIRT)
            return;

        finishQuest(event.getPlayer());
    }

    @Override
    public void onComplete(Player player) {
        player.sendMessage("COMPLETE");
    }
}
