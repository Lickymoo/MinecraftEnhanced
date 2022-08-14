package com.buoobuoo.minecraftenhanced.core.quest.impl.act1;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.entity.impl.ItemDropEntity;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.AramoreBlacksmithNpc;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.event.questrelated.ACT1_MQ2PickItemEvent;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.item.CustomItems;
import com.buoobuoo.minecraftenhanced.core.item.impl.quest.act1.ACT1_MQ3_NecklaceItem;
import com.buoobuoo.minecraftenhanced.core.quest.QuestLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ACT1_MQ3 extends QuestLine {
    public ACT1_MQ3(MinecraftEnhanced plugin) {
        super(plugin, "Foul Play", "ACT1_MQ3", "Find the missing expedition");

        execute(player -> {
            Location spawnLoc = new Location(MinecraftEnhanced.getMainWorld(), 83.5,  66, -161.5);

            EntityManager entityManager = plugin.getEntityManager();
            CustomItemManager customItemManager = plugin.getCustomItemManager();

            ItemStack itemStack = customItemManager.getItem(null, CustomItems.ACT1_MQ3_NECKLACE);
            ItemDropEntity necklace = (ItemDropEntity) entityManager.instantiateEntity(ItemDropEntity.class, spawnLoc, new Class<?>[]{ItemStack.class}, itemStack);

            Entity bukkitEntity = necklace.asEntity().getBukkitEntity();

            bukkitEntity.setGravity(false);
            bukkitEntity.setVelocity(new Vector(0, 0, 0));
            bukkitEntity.setCustomName(itemStack.getItemMeta().getDisplayName());
            bukkitEntity.setCustomNameVisible(true);

            necklace.setInvertHide(true);
            necklace.hideToPlayer(player);
            entityManager.spawnInstance(necklace, spawnLoc);
        });
        whenPickupCustomItem(ACT1_MQ3_NecklaceItem.class);
        execute(p -> {});
        checkpoint("&7Return to the Blacksmith in Aramore.");
        whenNpcInteract(AramoreBlacksmithNpc.class);
        finish();

    }

    @EventHandler
    public void pickItem(ACT1_MQ2PickItemEvent event){
        setDeterminant(event.getPlayer(), "PICKED_ITEM", true);
    }
}
