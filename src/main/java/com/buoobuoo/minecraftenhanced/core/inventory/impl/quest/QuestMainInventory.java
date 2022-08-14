package com.buoobuoo.minecraftenhanced.core.inventory.impl.quest;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.quest.QuestLine;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class QuestMainInventory extends CustomInventory {
    public QuestMainInventory(MinecraftEnhanced plugin, Player player) {
        super(plugin, player, "&8Quests", 36);
    }



    @Override
    public Inventory getInventory() {
        QuestManager questManager = plugin.getQuestManager();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);

        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));
        if(profileData.getActiveQuests() != null)
        for(String str : profileData.getActiveQuests()){
            String id = str.split(":")[0];
            QuestLine questLine = questManager.getQuestByID(id);
            if(questLine == null)
                continue;

            ItemStack item = new ItemBuilder(Material.BOOK).name(questLine.getQuestName()).lore(questLine.getQuestBrief()).create();
            inv.addItem(item);
        }

        if(profileData.getCompletedQuest() != null)
        for(String str : profileData.getCompletedQuest()){
            QuestLine questLine = questManager.getQuestByID(str);

            if(questLine == null)
                continue;

            ItemStack item = new ItemBuilder(Material.BOOK).name(questLine.getQuestName()).lore(questLine.getQuestBrief()).lore(10, "&a&lCOMPLETED").create();
            inv.addItem(item);
        }

        return inv;
    }
}
