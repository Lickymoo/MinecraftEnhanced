package com.buoobuoo.minecraftenhanced.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.command.impl.*;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.item.CustomItems;
import com.buoobuoo.minecraftenhanced.core.util.EnumUtil;
import com.buoobuoo.minecraftenhanced.permission.PermissionGroup;
import org.bukkit.entity.EntityType;

public class CommandManager {
    private final MinecraftEnhanced plugin;
    private final PaperCommandManager acfManager;

    public CommandManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
        this.acfManager = new PaperCommandManager(plugin);

        //item command
        acfManager.getCommandCompletions().registerAsyncCompletion("custom-items", c -> EnumUtil.getAllList(CustomItems.class));
        acfManager.getCommandCompletions().registerAsyncCompletion("abilities", c -> plugin.getAbilityManager().allAbilityID());
        acfManager.getCommandCompletions().registerAsyncCompletion("quests", c -> plugin.getQuestManager().allQuestID());
        acfManager.getCommandCompletions().registerAsyncCompletion("perm-groups",  c -> EnumUtil.getAllList(PermissionGroup.class));


        acfManager.getCommandCompletions().registerAsyncCompletion("custom-entities", c -> plugin.getEntityManager().getAllHandlerNames());
        acfManager.getCommandContexts().registerContext(CustomEntity.class, c -> plugin.getEntityManager().getHandlerByName(c.popFirstArg()));

        acfManager.getCommandCompletions().registerAsyncCompletion("vanilla-entity-type", c -> EnumUtil.getAllList(EntityType.class));
        acfManager.getCommandContexts().registerContext(EntityType.class, c -> EntityType.valueOf(c.popFirstArg().toUpperCase()));

        registerCommand(new EnhCommand(plugin));
        registerCommand(new PartyCommand(plugin));
        registerCommand(new PermissionCommand(plugin));
        registerCommand(new DialogueCommand(plugin));
        registerCommand(new QuestCommand(plugin));
        registerCommand(new ProfileCommand(plugin));
    }

    private void registerCommand(BaseCommand... cmds) {
        for(BaseCommand cmd : cmds) {
            acfManager.registerCommand(cmd);
        }
    }
}
