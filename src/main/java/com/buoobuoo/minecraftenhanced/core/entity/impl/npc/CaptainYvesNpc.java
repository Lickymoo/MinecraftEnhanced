package com.buoobuoo.minecraftenhanced.core.entity.impl.npc;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueManager;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueSection;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueTrack;
import com.buoobuoo.minecraftenhanced.core.dialogue.ExecutableSection;
import com.buoobuoo.minecraftenhanced.core.entity.AbstractNpc;
import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CaptainYvesNpc extends AbstractNpc {

    public CaptainYvesNpc(Location loc) {
        super(loc);
    }

    @Override
    public void onInteract(PlayerInteractNpcEvent event) {
        Player player = event.getPlayer();

        DialogueManager dialogueManager = MinecraftEnhanced.getInstance().getDialogueManager();
        QuestManager questManager = MinecraftEnhanced.getInstance().getQuestManager();

        DialogueTrack track = new DialogueTrack();
        track.addSection(
                new DialogueSection(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "Well " + player.getDisplayName() + ", it looks like the seas are rough!"),
                new ExecutableSection(inst -> {
                    Util.sendDialogueBox(player, CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "WOAH!?");
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10000, 10000));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10000, 10000));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(MinecraftEnhanced.getInstance(), () -> {
                        questManager.completeQuest(player, questManager.getQuestByID("INTRO_1_QUEST"));
                    }, 40);
                })
        );

        dialogueManager.startDialogue(track, player);
    }

    @Override
    public String entityID() {
        return "NPC_CAPTAIN_YVES";
    }

    @Override
    public String entityName() {
        return "Captain Yves";
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
