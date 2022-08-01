package com.buoobuoo.minecraftenhanced.core.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.command.impl.*;
import com.buoobuoo.minecraftenhanced.core.item.CustomItems;
import com.buoobuoo.minecraftenhanced.core.npc.Npcs;
import com.buoobuoo.minecraftenhanced.core.permission.PermissionGroup;
import com.buoobuoo.minecraftenhanced.core.util.EnumUtil;

public class CommandManager {
    private final MinecraftEnhanced plugin;
    private final PaperCommandManager acfManager;

    public CommandManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
        this.acfManager = new PaperCommandManager(plugin);

        //item command
        acfManager.getCommandCompletions().registerAsyncCompletion("custom-items", c -> EnumUtil.getAllList(CustomItems.class));
        acfManager.getCommandCompletions().registerAsyncCompletion("npcs", c -> EnumUtil.getAllList(Npcs.class));
        acfManager.getCommandCompletions().registerAsyncCompletion("quests", c -> plugin.getQuestManager().allQuestID());
        acfManager.getCommandCompletions().registerAsyncCompletion("perm-groups",  c -> EnumUtil.getAllList(PermissionGroup.class));

        registerCommand(new EnhCommand(plugin));
        registerCommand(new PartyCommand(plugin));
        registerCommand(new PermissionCommand(plugin));
        registerCommand(new DialogueCommand(plugin));
        registerCommand(new QuestCommand(plugin));
    }

    private void registerCommand(BaseCommand... cmds) {
        for(BaseCommand cmd : cmds) {
            acfManager.registerCommand(cmd);
        }
    }
}
