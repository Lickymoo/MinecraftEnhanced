package com.buoobuoo.minecraftenhanced.core.quest.impl.story.intro;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.impl.AramoreArea;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.HelpfulNpc;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.event.AreaEnterEvent;
import com.buoobuoo.minecraftenhanced.core.navigation.Route;
import com.buoobuoo.minecraftenhanced.core.quest.Quest;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicFrame;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicSequence;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.SpectatorManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        player.teleport(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 195, 51, 282, -180, 0));
        player.resetPlayerWeather();

        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.CONFUSION);

        EntityManager entityManager = plugin.getEntityManager();

        Route route = new Route(
                new Location(MinecraftEnhanced.getMainWorld(), 194, 53, 261),
                new Location(MinecraftEnhanced.getMainWorld(), 195, 52, 274)
        );

        SpectatorManager spectatorManager = plugin.getSpectatorManager();
        //After npc has ran to player
        route.setOnComplete(() -> {
            Util.sendDialogueBox(player, CharRepo.UI_PORTRAIT_BOUNCER_TUFF, "&7???", "Hey! Wake up. You alright?");
            spectatorManager.stopSpectatorMode(player);
        });

       // spectatorManager.viewLoc(player, player.getLocation());
        CustomEntity helpfulNpc = entityManager.spawnEntity(HelpfulNpc.class, new Location(MinecraftEnhanced.getMainWorld(), 194, 53, 261), new Class<?>[]{Player.class}, player);
        CinematicSequence sequence = new CinematicSequence(plugin, true,
                new CinematicFrame(1, pl -> {
                    entityManager.getRoutePlanner().setRoute(helpfulNpc.getPathfinderMob(), route);}),
                new CinematicFrame(60, pl -> {})
        );
        sequence.execute(player);
    }

    @Override
    public void onComplete(Player player) {
        System.out.println("COMPLETE");
    }
}
