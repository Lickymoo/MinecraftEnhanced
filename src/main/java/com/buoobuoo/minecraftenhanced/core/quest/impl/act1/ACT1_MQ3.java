package com.buoobuoo.minecraftenhanced.core.quest.impl.act1;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.impl.aramore.AramoreAbandonedShrineArea;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.entity.impl.ItemDropEntity;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.AramoreBlacksmithNpc;
import com.buoobuoo.minecraftenhanced.core.entity.impl.util.EmptyEntity;
import com.buoobuoo.minecraftenhanced.core.event.questrelated.ACT1_MQ2PickItemEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.impl.questspecific.ACT1_MQ2Inventory;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.item.CustomItems;
import com.buoobuoo.minecraftenhanced.core.item.impl.quest.act1.ACT1_MQ2_NecklaceItem;
import com.buoobuoo.minecraftenhanced.core.quest.QuestLine;
import com.buoobuoo.minecraftenhanced.core.util.LocationUtil;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicFrame;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicSequence;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ACT1_MQ3 extends QuestLine {
    public ACT1_MQ3(MinecraftEnhanced plugin) {
        super(plugin, "Anomaly", "ACT1_MQ3", "");

        whenEnterArea(AramoreAbandonedShrineArea.class);

        cinematic(player -> {
            Location startLoc = new Location(MinecraftEnhanced.getMainWorld(), -43, 118, -103, 100, 40);
            Location endLoc = new Location(MinecraftEnhanced.getMainWorld(), -53, 118, -92, 150, 44);
            EmptyEntity stand = plugin.getSpectatorManager().viewLoc(player, startLoc);
            CinematicFrame[] move1 = LocationUtil.lerp(startLoc, endLoc, 100, stand);
            return new CinematicSequence(plugin, true, CinematicSequence.mergeArrays(move1));
        });
        finish();


    }

    @EventHandler
    public void pickItem(ACT1_MQ2PickItemEvent event){
        setDeterminant(event.getPlayer(), "PICKED_ITEM", true);
    }
}
