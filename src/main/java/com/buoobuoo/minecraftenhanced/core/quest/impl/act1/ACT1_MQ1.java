package com.buoobuoo.minecraftenhanced.core.quest.impl.act1;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.entity.impl.RatEntity;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.CaptainYvesNpc;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.HelpfulNpc;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.navigation.RouteSingularPlayer;
import com.buoobuoo.minecraftenhanced.core.quest.QuestLine;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ACT1_MQ1 extends QuestLine {
    public ACT1_MQ1(MinecraftEnhanced plugin) {
        super(plugin, "Washed up", "ACT1_MQ1", "Find signs of life");

        //whenever
        whenever("OVERBOARD", player -> {
            player.teleport(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 185.5, 53, 640, -160, 0));
            Util.sendDialogueBox(player, CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Woah there " + player.getName() + "! You almost drowned.");
        });

        //main story
        execute(player -> {
            EntityManager entityManager = plugin.getEntityManager();

            Location spawnLoc = new Location(MinecraftEnhanced.getMainWorld(), 188.5, 53, 628.5);

            CustomEntity captainYvesNpc = entityManager.instantiateEntity(CaptainYvesNpc.class, spawnLoc);
            captainYvesNpc.setInvertHide(true);
            captainYvesNpc.hideToPlayer(player);
            entityManager.spawnInstance(captainYvesNpc, spawnLoc);

            putObject("CAPTAIN_YVES", captainYvesNpc, player);

            player.teleport(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 185.5, 53, 640, -160, 0));
            player.setPlayerWeather(WeatherType.DOWNFALL);
        });
        whenNpcInteract(CaptainYvesNpc.class);
        dialogueNext(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves \nSeas are rough. Going like this it's going to take us 5 days to get to Onyrx");
        dialogueNext(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves \nFood's running low too. Chances are, starvation'll take us before Onryx takes us in.");
        dialogueNext(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves \nEven if they do, refugees like us will only be allowed to live out in the ghettos. Still better than nothing");
        dialogueNext(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves \nWait... you hear that? The bow's starting to rock...");
        dialogue(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves \nWOAH!?", 20);
        execute(player -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10000, 10000));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10000, 10000));
        });
        execute(player -> {
            CustomEntity captainYvesNpc = getObject(CustomEntity.class, "CAPTAIN_YVES", player);
            EntityManager entityManager = plugin.getEntityManager();

            entityManager.removeEntity(captainYvesNpc);
        });
        marker("OFF_SHIP");
        execute(player -> {
            player.teleport(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 195, 51, 282, -180, 0));
            player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        });
        checkpoint();
        delay(40);
        execute(player -> {
            player.resetPlayerWeather();

            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.removePotionEffect(PotionEffectType.CONFUSION);
        });
        execute(player -> {
            EntityManager entityManager = plugin.getEntityManager();

            RouteSingularPlayer route = new RouteSingularPlayer(
                    new Location(MinecraftEnhanced.getMainWorld(), 194, 53, 261),
                    new Location(MinecraftEnhanced.getMainWorld(), 195, 52, 274)
            );

            //After npc has ran to player
            Location spawnLoc = new Location(MinecraftEnhanced.getMainWorld(), 194, 53, 261);
            CustomEntity helpfulNpc = entityManager.instantiateEntity(HelpfulNpc.class, spawnLoc);
            helpfulNpc.setInvertHide(true);
            helpfulNpc.hideToPlayer(player);
            entityManager.spawnInstance(helpfulNpc, spawnLoc);
            entityManager.getRoutePlanner().setRoute(helpfulNpc.getPathfinderMob(), route);

            putObject("HELPFUL_NPC", helpfulNpc, player);
            putObject("HELPFUL_NPC_ROUTE", route, player);
        });
        whenRouteComplete("HELPFUL_NPC_ROUTE");
        dialogue(CharRepo.UI_PORTRAIT_JAYCE, "??? \nHey! Wake up. You alright?", 60);
        whenNpcInteract(HelpfulNpc.class);
        dialogueNext(CharRepo.UI_PORTRAIT_JAYCE, "??? \nMighty storm we had last night, must've tossed you and your barge all around the coast.");
        dialogueNext(CharRepo.UI_PORTRAIT_JAYCE, "??? \nLook at you, all tattered and torn... Follow me. Just a short walk and my settlement will have you rested up and clothed in no time");
        execute(player -> {
            RouteSingularPlayer route = new RouteSingularPlayer(
                    new Location(MinecraftEnhanced.getMainWorld(), 197, 66, 218),
                    new Location(MinecraftEnhanced.getMainWorld(), 197, 66, 168)
            );

            CustomEntity helpfulNpc = getObject(CustomEntity.class, "HELPFUL_NPC", player);
            EntityManager entityManager = plugin.getEntityManager();

            entityManager.getRoutePlanner().setRoute(helpfulNpc.getPathfinderMob(), route);
            putObject("HELPFUL_NPC_ROUTE", route, player);
        });
        whenRouteComplete("HELPFUL_NPC_ROUTE");
        dialogue(CharRepo.UI_PORTRAIT_JAYCE, "??? \nIs it just me, or is that cart moving?");
        delay(20);
        dialogue(CharRepo.UI_PORTRAIT_JAYCE, "??? \nGah! Look at the size of those rats! Hurry, kill them or they'll keep gorging on the settlement's food!");
        execute(player -> {
            EntityManager entityManager = plugin.getEntityManager();
            Location spawnLoc = new Location(MinecraftEnhanced.getMainWorld(), 200, 66, 167);
            RatEntity rat = (RatEntity) entityManager.instantiateEntity(RatEntity.class, spawnLoc);

            //invert hide
            rat.setInvertHide(true);
            rat.hideToPlayer(player);
            entityManager.spawnInstance(rat, spawnLoc);
        });
        whenKillEntity(RatEntity.class);
        dialogue(CharRepo.UI_PORTRAIT_JAYCE, "??? \nPhew. Farmer Ivoy would have killed me if we lost any more of his grain. Come on, let's get going.");
        delay(10);
        execute(player -> {
            RouteSingularPlayer route = new RouteSingularPlayer(
                    new Location(MinecraftEnhanced.getMainWorld(), 189, 66, 155),
                    new Location(MinecraftEnhanced.getMainWorld(), 186, 66, 133),
                    new Location(MinecraftEnhanced.getMainWorld(), 186, 66, 110)
            );

            CustomEntity helpfulNpc = getObject(CustomEntity.class, "HELPFUL_NPC", player);
            EntityManager entityManager = plugin.getEntityManager();

            entityManager.getRoutePlanner().setRoute(helpfulNpc.getPathfinderMob(), route);
            putObject("ROUTE_TO_TOWN", route, player);
        });
        whenRouteComplete("ROUTE_TO_TOWN");
        dialogueNext(CharRepo.UI_PORTRAIT_JAYCE, "??? \nWelcome to Aramore! More of a humble hamlet than a village, but nevertheless our home");
        dialogueNext(CharRepo.UI_PORTRAIT_JAYCE, "??? \nBut as you saw earlier, it’s not quite safe out here. Head to the blacksmith and get yourself outfitted before you get bitten all over your ankles. Tell him Jayce sent you.");
        dialogueNext(CharRepo.UI_PORTRAIT_JAYCE, "Jayce \nAnyways, I’ve got to go and get back to work. Feel free to stop by for a chat any time.");

        execute(player -> {
            CustomEntity helpfulNpc = getObject(CustomEntity.class, "HELPFUL_NPC", player);
            EntityManager entityManager = plugin.getEntityManager();

            entityManager.removeEntity(helpfulNpc);
        });
        finish(ACT1_MQ2.class);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(!isApplicable(player))
            return;

        if(!isBeforeMarker(player, "OFF_SHIP"))
            return;

        Location loc = player.getLocation();
        if (loc.getY() <= 50)
            setDeterminant(player, "OVERBOARD", true);
    }
}
