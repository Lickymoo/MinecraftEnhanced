package com.buoobuoo.minecraftenhanced.core.item.additional.requirements;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ItemRequirementManager {
    private final MinecraftEnhanced plugin;

    public ItemRequirementManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    public List<ItemRequirement> getAttribInstances(ItemStack item){
        CustomItem handler = plugin.getCustomItemManager().getRegistry().getHandler(item);
        if(!(handler instanceof AttributedItem))
            return null;

        AttributedItem aHandler = (AttributedItem)handler;
        return aHandler.getRequirements();

    }
}
