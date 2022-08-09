package com.buoobuoo.minecraftenhanced.core.entity.impl.npc;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueManager;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueSection;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueTrack;
import com.buoobuoo.minecraftenhanced.core.dialogue.ExecutableSection;
import com.buoobuoo.minecraftenhanced.core.entity.AbstractSingularViewerNpc;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import com.buoobuoo.minecraftenhanced.core.navigation.Route;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HelpfulNpc extends AbstractSingularViewerNpc {

    public HelpfulNpc(Location loc, Player player) {
        super(loc, player.getUniqueId());
    }

    @Override
    public void onInteract(PlayerInteractNpcEvent event) {
        Player player = event.getPlayer();

        DialogueManager dialogueManager = MinecraftEnhanced.getInstance().getDialogueManager();
        QuestManager questManager = MinecraftEnhanced.getInstance().getQuestManager();
        EntityManager entityManager = MinecraftEnhanced.getInstance().getEntityManager();

        if(questManager.playerHasCompletedQuest(player, questManager.getQuestByID("INTRO_2_QUEST")))
            return;

        DialogueTrack track = new DialogueTrack();
        track.addSection(
                new DialogueSection(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "???", "Mighty storm we had last night, must've tossed you", "and your barge all around the coast."),
                new DialogueSection(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "???", "Look at you, all tattered and torn...", "Follow me. Just a short walk and my settlement", "will have you rested up and clothed in no time"),
                new ExecutableSection(inst -> {
                    Route route = new Route(
                            new Location(MinecraftEnhanced.getMainWorld(), 197, 66, 218),
                            new Location(MinecraftEnhanced.getMainWorld(), 197, 66, 168)
                    );
                    entityManager.getRoutePlanner().setRoute(getPathfinderMob(), route);

                })

        );

        dialogueManager.startDialogue(track, player);
    }


    @Override
    public String entityID() {
        return "NPC_HELPFUL_NPC";
    }

    @Override
    public String entityName() {
        return "???";
    }

    @Override
    public double maxHealth() {
        return 10;
    }

    @Override
    public double damage() {
        return 0;
    }

    @Override
    public double tagOffset() {
        return 0;
    }

    @Override
    public int entityLevel() {
        return 0;
    }

    @Override
    public boolean showHealth() {
        return false;
    }

    @Override
    public String overrideTag() {
        return CharRepo.SPEECH.getCh();
    }

    @Override
    public String textureSignature() {
        return "";
    }

    @Override
    public String textureBase64() {
        return "";
    }
}
