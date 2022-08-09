package com.buoobuoo.minecraftenhanced.rework.quest.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueManager;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueSection;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueTrack;
import com.buoobuoo.minecraftenhanced.core.dialogue.ExecutableSection;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.CaptainYvesNpc;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.HelpfulNpc;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.navigation.Route;
import com.buoobuoo.minecraftenhanced.core.navigation.RouteSingularPlayer;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicFrame;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicSequence;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.SpectatorManager;
import com.buoobuoo.minecraftenhanced.rework.quest.QuestLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TestQuest extends QuestLine {
    public TestQuest(MinecraftEnhanced plugin) {
        super(plugin, "", "TEST_QUEST");

        RouteSingularPlayer runToPlayerRoute = new RouteSingularPlayer(
                new Location(MinecraftEnhanced.getMainWorld(), 194, 53, 261),
                new Location(MinecraftEnhanced.getMainWorld(), 195, 52, 274)
        );

        RouteSingularPlayer routeToRats = new RouteSingularPlayer(
                new Location(MinecraftEnhanced.getMainWorld(), 197, 66, 218),
                new Location(MinecraftEnhanced.getMainWorld(), 197, 66, 168)
        );

        execute(player -> {
            player.teleport(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 187, 52, 631));
            player.setPlayerWeather(WeatherType.DOWNFALL);
        });
        whenNpcInteract(CaptainYvesNpc.class);
        dialogueNext(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "Seas are rough. Going like this,", "it's going to take us 5 days", "to get to Onyrx");
        dialogueNext(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "Food's running low too.", "Chances are, starvation'll take us", "before Onryx takes us in.");
        dialogueNext(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "Even if they do, refugees like us will", "only be allowed to live out in the ghettos.", "Still better than nothing");
        dialogueNext(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "Wait... you hear that? The bow's starting to rock...");
        dialogue(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "WOAH!?");
        execute(player -> {
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10000, 10000));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10000, 10000));
            player.teleport(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 195, 51, 282, -180, 0));
        });
        execute(player -> {
            player.resetPlayerWeather();

            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.removePotionEffect(PotionEffectType.CONFUSION);
        });
        execute(player -> {
            EntityManager entityManager = plugin.getEntityManager();

            //After npc has ran to player
            CustomEntity helpfulNpc = entityManager.spawnEntity(HelpfulNpc.class, new Location(MinecraftEnhanced.getMainWorld(), 194, 53, 261), new Class<?>[]{Player.class}, player);
            entityManager.getRoutePlanner().setRoute(helpfulNpc.getPathfinderMob(), runToPlayerRoute);

            putObject("HELPFUL_NPC", helpfulNpc, player);
        });
        whenRouteComplete(runToPlayerRoute);
        dialogue(CharRepo.UI_PORTRAIT_BOUNCER_TUFF, "???", "Hey! Wake up. You alright?");
        whenNpcInteract(HelpfulNpc.class);
        dialogueNext(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "???", "Mighty storm we had last night, must've tossed you", "and your barge all around the coast.");
        dialogueNext(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "???", "Look at you, all tattered and torn...", "Follow me. Just a short walk and my settlement", "will have you rested up and clothed in no time");
        execute(player -> {
            CustomEntity helpfulNpc = getObject(CustomEntity.class, "HELPFUL_NPC", player);
            EntityManager entityManager = plugin.getEntityManager();

            entityManager.getRoutePlanner().setRoute(helpfulNpc.getPathfinderMob(), routeToRats);
        });
        whenRouteComplete(routeToRats);

        finish();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        setDeterminant(event.getPlayer(), "move", true);
    }
}
