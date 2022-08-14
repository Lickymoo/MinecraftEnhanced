package com.buoobuoo.minecraftenhanced.core.quest.impl.act1;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.AramoreBlacksmithNpc;
import com.buoobuoo.minecraftenhanced.core.event.questrelated.ACT1_MQ2PickItemEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.impl.questspecific.ACT1_MQ2Inventory;
import com.buoobuoo.minecraftenhanced.core.quest.QuestLine;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;

public class ACT1_MQ2 extends QuestLine {
    public ACT1_MQ2(MinecraftEnhanced plugin) {
        super(plugin, "Hamlet on the Coast", "ACT1_MQ2", "");

        whenNpcInteract(AramoreBlacksmithNpc.class);
        dialogueNext(CharRepo.UI_PORTRAIT_ARAMORE_BLACKSMITH, "Blacksmith \n ...I don't recognise you.");
        dialogueNext(CharRepo.UI_PORTRAIT_ARAMORE_BLACKSMITH, "Blacksmith \n Ah, Jayce sent you? Well, sorry to say but you aren't getting gear for free. Jayce still owes me for an axe, 4 weeks overdue");
        dialogueNext(CharRepo.UI_PORTRAIT_ARAMORE_BLACKSMITH, "Blacksmith \n However, there is something you can do for me... tel you what; a week ago, an expedition party of ours headed North and we haven't heard from them since.");
        dialogueNext(CharRepo.UI_PORTRAIT_ARAMORE_BLACKSMITH, "Blacksmith \n In exchange for a weapon, I need you to go and investigate. The rest of us are busy at work, but we're starting to get worried.");
        checkpoint();
        execute(player -> {
           Inventory inv = new ACT1_MQ2Inventory(plugin, player).getInventory();
           player.openInventory(inv);
        });
        when("PICKED_ITEM");
        checkpoint();
        finish(ACT1_MQ3.class);


    }

    @EventHandler
    public void pickItem(ACT1_MQ2PickItemEvent event){
        setDeterminant(event.getPlayer(), "PICKED_ITEM", true);
    }
}
