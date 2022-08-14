package com.buoobuoo.minecraftenhanced.core.item.impl.quest.act1;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.ItemLevel;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.NotStackable;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.QuestItem;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import org.bukkit.Material;

public class ACT1_MQ3_NecklaceItem extends CustomItem implements QuestItem, NotStackable {
    public ACT1_MQ3_NecklaceItem() {
        super("ACT1_MQ3_NECKLACE", Material.EMERALD, "Expeditioners necklace");
    }

    @Override
    public void modifierCreate(MinecraftEnhanced plugin, ItemBuilder ib) {
        NotStackable.super.modifierCreate(plugin, ib);
        QuestItem.super.modifierCreate(plugin, ib);
    }
}
