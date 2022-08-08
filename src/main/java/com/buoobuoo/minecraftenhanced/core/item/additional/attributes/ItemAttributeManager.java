package com.buoobuoo.minecraftenhanced.core.item.additional.attributes;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemAttributeManager {
    private final MinecraftEnhanced plugin;

    public ItemAttributeManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    public List<ItemAttributeInstance> getAttribInstances(ItemStack item){
        CustomItem handler = plugin.getCustomItemManager().getRegistry().getHandler(item);
        if(!(handler instanceof AttributedItem))
            return null;

        AttributedItem aHandler = (AttributedItem)handler;

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        String list = pdc.get(new NamespacedKey(plugin, "ATTRIB_LIST"), PersistentDataType.STRING);

        List<ItemAttributeInstance> instances = new ArrayList<>();
        String[] attribs = list.split(",");

        for(String attrib : attribs){
            String id = attrib.split(":")[0];
            double val = Double.parseDouble(attrib.split(":")[1]);

            ItemAttribute attribute = aHandler.getItemAttribByID(id);
            if(attribute == null)
                continue;

            ItemAttributeInstance instance = new ItemAttributeInstance(attribute, val);
            instances.add(instance);
        }
        return instances;

    }
}
