package com.buoobuoo.minecraftenhanced.core.npc.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueManager;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueSection;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueTrack;
import com.buoobuoo.minecraftenhanced.core.dialogue.ExecutableSection;
import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import com.buoobuoo.minecraftenhanced.core.npc.Npc;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import org.bukkit.entity.Player;

public class JaydenNpc extends Npc {
    public JaydenNpc() {
        super(
                "\uF001",
                "BOO9Gv8DIgAy5Hjy2w/s5yrHHx9U7jSzIpWRh2YWewpGBstawW5D5PoQpRlZfPIlvUWMGD6FSToyZZ74zcUBuKtxcgpVWYLHmGw51DQPPhLNlu7QFoGrE5AUuVwW7DTPHC1CP47/t79QO/SeSM1PKZR8bPV7ikih3CyGoz62sPq+iKQQY7AUmpwNNexnjpwHdbW8VE2BajOlqD8wnoLauqmfpur2ZIoU6XbNQjSG41Fpw+YsEZEOl/hw4nP1u4VP4w6xGAcwl0xml0H6MaduHhWTF8JVobM8SvuQBNINGRbY/YBW6lVVyB5GIQ60cXICngvzr+09VcWY9KDWEnM9IAmC9nsBdIOGh+aKbWErP+pAWqJ2qczuvH4okW8apSDVqssOKGTmpCwIIaP5LMLoPESpWJu78BYaeQwG0j2OQ1tPI5BetmJrNPRuwKhbBQA2bXgDydFRIKNps9bS0BWUBf+yMuYLaof8uY7BWewrjjagRHS/c8MD2Q8aG82X0OQS/cZFsU9tL3EsAdZps4kCEEpPFJDRvpglIRL2v4WDiQBcUKpLzC9RA4Qw6Z/eo7nI4C3XAwN7BJQBKgBJcsm2JSGK6DMKnhFqFKKlCSAvjtEAt2FwayumHyp3K9lybTRBsEyJGFV9dPonzgBBokXKvABH/Evs20KvYULMA0VZtcs=",
                "ewogICJ0aW1lc3RhbXAiIDogMTY1NjU4MzMwODE2NywKICAicHJvZmlsZUlkIiA6ICI4N2RlZmVhMTQwMWQ0MzYxODFhNmNhOWI3ZGQ2ODg0MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTcGh5bnhpdHMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZkNmMyZWQ4Nzc2ODI4OTU2MDg2YjUzYWE2NzAxOTAwYmQ3ZDQ4ZGNmZmJiNDcyNjJmZWZiZTdjNmQ3OTZlYiIKICAgIH0KICB9Cn0"
        );
    }

    @Override
    public void onInteract(PlayerInteractNpcEvent event) {
        Player player = event.getPlayer();

        DialogueManager dialogueManager = MinecraftEnhanced.getInstance().getDialogueManager();
        QuestManager questManager = MinecraftEnhanced.getInstance().getQuestManager();

        DialogueTrack track = new DialogueTrack();
        track.addSection(
                new DialogueSection(CharRepo.UI_PORTRAIT_BOUNCER_TUFF, "TEST DIALOGUE"),
                new ExecutableSection(inst -> {
                    Player pl = inst.getPlayer();
                    questManager.beginQuest(pl, questManager.getQuestByID("INTRO_QUEST"));
                })
        );

        dialogueManager.startDialogue(track, player);
    }

}
