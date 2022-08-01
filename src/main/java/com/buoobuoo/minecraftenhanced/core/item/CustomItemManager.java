package com.buoobuoo.minecraftenhanced.core.item;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.BlockItem;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

@Getter
public class CustomItemManager implements Listener {

    private final MinecraftEnhanced plugin;
    private final CustomItemRegistry registry;

    public CustomItemManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
        this.registry = new CustomItemRegistry(plugin);
    }

    public ItemStack getItem(CustomItems item){
        return getItem(item.getHandler());
    }

    public ItemStack getItem(CustomItem item){

        ItemStack itemStack = new ItemStack(item.getMaterial());
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Util.formatColour(Arrays.asList(item.getLore())));
        meta.setDisplayName(Util.formatColour("&r" + item.getDisplayName()));

        if(item.getCustomModelData() != 0)
            meta.setCustomModelData(item.getCustomModelData());

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "ITEM_ID");
        pdc.set(key, PersistentDataType.STRING, item.getId());

        itemStack.setItemMeta(meta);
        return itemStack;
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








































