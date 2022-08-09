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

        if(questManager.playerHasCompletedQuest(player, questManager.getQuestByID("INTRO_1_QUEST")))
            return;

        DialogueTrack track = new DialogueTrack();
        track.addSection(
                new DialogueSection(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "Seas are rough. Going like this,", "it's going to take us 5 days", "to get to Onyrx"),
                new DialogueSection(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "Food's running low too.", "Chances are, starvation'll take us", "before Onryx takes us in."),
                new DialogueSection(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "Even if they do, refugees like us will", "only be allowed to live out in the ghettos.", "Still better than nothing"),
                new DialogueSection(CharRepo.UI_PORTRAIT_CAPTAIN_YVES, "Captain Yves", "Wait... you hear that? The bow's starting to rock..."),
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
