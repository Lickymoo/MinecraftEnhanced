package com.buoobuoo.minecraftenhanced.core.item.interfaces;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.attributes.ItemAttribute;
import com.buoobuoo.minecraftenhanced.core.item.attributes.ItemAttributeInstance;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AttributedItem extends CustomItem {

    public List<ItemAttribute> attributes = new ArrayList<>();

    public AttributedItem(String id, Material material, String displayName, String... lore) {
        super(id, material, displayName, lore);
    }

    public AttributedItem(String id, Material material, int customModelData, String displayName, String... lore) {
        super(id, material, customModelData, displayName, lore);
    }

    public void addAttributes(ItemAttribute... attributes){
        this.attributes.addAll(List.of(attributes));
    }

    @Override
    public void onCreate(MinecraftEnhanced plugin, ItemBuilder ib){
        super.onCreate(plugin, ib);
        List<String> attribValues = new ArrayList<>();
        for(ItemAttribute attribute : attributes){
            ItemAttributeInstance inst = new ItemAttributeInstance(attribute);
            attribValues.add(inst.getAttributeString());
            ib.lore(1, attribute.itemLore(inst));
        }

        ib.nbtString(plugin, "ATTRIB_LIST", Util.fromList(attribValues));
    }
}

































