package com.buoobuoo.minecraftenhanced.core.item;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.BlockItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.Modifier;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.NotStackable;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CustomItemManager implements Listener {

    private final MinecraftEnhanced plugin;
    private final CustomItemRegistry registry;
    private final List<Class<? extends Modifier>> modifierHandlers = new ArrayList<>();

    public CustomItemManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
        this.registry = new CustomItemRegistry(plugin);

        this.modifierHandlers.add(NotStackable.class);
    }

    public ItemStack getItem(CustomItems item){
        return getItem(item.getHandler());
    }

    public ItemStack getItem(CustomItem item){

        ItemBuilder ib = new ItemBuilder(item.getMaterial());
        ib.name(item.getRarity().getColor() + item.getDisplayName());
        item.onCreate(plugin, ib);

        if(item.getCustomModelData() != 0)
            ib.setCustomModelData(item.getCustomModelData());

        ib.nbtString(plugin, "ITEM_ID", item.getId());

        List<Class<?>> interfaces = List.of(item.getClass().getInterfaces());
        for(Class<? extends Modifier> cl : modifierHandlers){
            if (interfaces.contains(cl)){
                Modifier modifier = cl.cast(item);
                modifier.modifierCreate(plugin, ib);
            }
        }

        return ib.create();
    }

    //Handler for placing a custom block (dont ask why its in this manager class lol)
    @EventHandler(priority = EventPriority.LOWEST)
    public void customBlockPlace(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if(!event.hasItem())
            return;

        Block block = event.getClickedBlock();
        ItemStack item = event.getItem();

        if(!registry.isCustomItem(item))
            return;

        CustomItem handler = registry.getHandler(item);
        if(handler instanceof BlockItem){
            BlockFace face = event.getBlockFace();
            Block relBlock = block.getRelative(face);

            BlockItem bI = (BlockItem) handler;

            bI.blockPlaced().getHandler().placeAt(relBlock, plugin, 1);
            player.swingMainHand();

            event.setCancelled(true);

            if(player.getGameMode() == GameMode.SURVIVAL){
                ItemStack hand = player.getInventory().getItemInMainHand();
                hand.setAmount(hand.getAmount() - 1);
            }
        }
    }
}








































