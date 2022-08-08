package com.buoobuoo.minecraftenhanced.core.quest.impl.story.intro;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.impl.AramoreArea;
import com.buoobuoo.minecraftenhanced.core.event.AreaEnterEvent;
import com.buoobuoo.minecraftenhanced.core.quest.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

public class Intro2Quest extends Quest {
    public Intro2Quest(MinecraftEnhanced plugin) {
        super(plugin, "INTRO_2_QUEST", "Washed up", "&7Find signs of life");
    }

    @EventHandler
    public void areaEnter(AreaEnterEvent event){
        if(!isApplicable(event.getPlayer()))
            return;

        if(event.getArea().getClass() != AramoreArea.class)
            return;

        finishQuest(event.getPlayer());
    }

    @Override
    public void onStart(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.CONFUSION);
    }

    @Override
    public void onComplete(Player player) {
        System.out.println("COMPLETE");
    }
}
