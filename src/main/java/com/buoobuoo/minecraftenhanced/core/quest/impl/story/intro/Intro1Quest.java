package com.buoobuoo.minecraftenhanced.core.quest.impl.story.intro;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.quest.Quest;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class Intro1Quest extends Quest {
    public Intro1Quest(MinecraftEnhanced plugin) {
        super(plugin, "INTRO_1_QUEST", "", "");
    }

    @Override
    public void onStart(Player player) {
        player.teleport(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 187, 52, 631));
        Location npcLoc = new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 187, 52, 626);
        player.setPlayerWeather(WeatherType.DOWNFALL);
    }

    @EventHandler
    public void moveEvent(PlayerMoveEvent event){
        if(!isApplicable(event.getPlayer()))
            return;

        if(event.getTo().getY() <= 50){
            Player player = event.getPlayer();
            Util.sendDialogueBox(player, CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "Be careful " + player.getDisplayName() + "! You almost drowned.");
            player.teleport(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 187, 52, 631));
        }
    }

    @Override
    public void onComplete(Player player) {
        player.teleport(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 195, 51, 282));
        player.resetPlayerWeather();
        questManager.beginQuest(player, questManager.getQuestByID("INTRO_2_QUEST"));
    }
}
