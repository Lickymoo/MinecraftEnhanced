package com.buoobuoo.minecraftenhanced.core.block;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemRegistry;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//Manager which aids in block breaking, interaction and other custom block related events
@Getter
public class CustomBlockManager implements Listener {

    private final MinecraftEnhanced plugin;
    private final CustomBlockRegistry registry;

    private static final List<BlockBreakInstance> breakMap = new ArrayList<>();

    public CustomBlockManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
        this.registry = new CustomBlockRegistry(plugin);

        Bukkit.getScheduler().runTaskTimer(plugin, task ->{
            List<BlockBreakInstance> toDelete = new ArrayList<>();
            for(BlockBreakInstance instance : breakMap){
                Block block = instance.getBlock();
                Player player = Bukkit.getPlayer(instance.getPlayerUUID());
                int maxTick = instance.getMaxTick();
                int currentTick = instance.getCurrentTick();

                int randId = instance.getRandId();
                Location loc = block.getLocation();

                double stage = ((double)currentTick/(double) maxTick) * 10d;

                BlockPos blockPos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                Util.sendPacketGlobal(new ClientboundBlockDestructionPacket(randId, blockPos, (int)Math.floor(stage)));

                if(currentTick >= maxTick){

                    //Call block break event as it wont be called
                    breakBlock(instance, player);

                    toDelete.add(instance);
                    continue;
                }
                instance.setCurrentTick(currentTick+1);
            }
            breakMap.removeAll(toDelete);
        }, 1, 1);
    }

    private void breakBlock(BlockBreakInstance instance, Player player){
        Block block = instance.getBlock();
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            if (instance.getCustomBlock() != null) {
                block.getWorld().playSound(block.getLocation(), instance.getCustomBlock().getBreakSound(), 1, 1);
                block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5,0.5,0.5), 50, 0.25, 0.25, 0.25, block.getBlockData());
            }

            block.setType(Material.AIR);
        }
    }

    @EventHandler
    public void blockDrop(BlockDropItemEvent event){
        if(event.getBlock().getType() != Material.NOTE_BLOCK)
            return;

        event.getItems().clear();
    }

    //Handler for clicking on the noteblock
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;

        if (!event.hasBlock())
            return;


        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.NOTE_BLOCK)
            return;

        //check if player is trying to place block
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null){
            ItemStack item = event.getItem();

            CustomItemRegistry registry = plugin.getCustomItemManager().getRegistry();
            if(item.getType().isSolid()) {
                Material placedType = item.getType();

                BlockFace face = event.getBlockFace();
                Block relBlock = block.getRelative(face);

                relBlock.setType(placedType);
                event.getPlayer().getInventory().getItemInMainHand().setAmount(item.getAmount()-1);
            }
        }
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            event.setCancelled(true);
        }
        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            //same as block break event
            event.setCancelled(true);

            Player player = event.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, Integer.MAX_VALUE));

            //berak handler
            CustomBlock handler = registry.getHandler(block);
            int ticks = Util.calculateTimeToBreak(player, player.getInventory().getItemInMainHand(), handler);
            ItemStack item = player.getInventory().getItemInMainHand();
            UUID uuid = player.getUniqueId();

            BlockBreakInstance instance = new BlockBreakInstance(block, handler, 0, ticks, item, uuid);
            breakMap.add(instance);
        }

        if (registry.isCustomBlock(block)) {
            CustomBlock handler = registry.getHandler(block);
            handler.onInteract(plugin, event);
        }
    }

    @EventHandler
    public void playerPlaceNoteBlock(BlockPlaceEvent event){
        if(event.getItemInHand().getType() == Material.NOTE_BLOCK) {
            //event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerStopBreakBlock(BlockDamageAbortEvent event){
        Block block = event.getBlock();
        if (block.getType() != Material.NOTE_BLOCK)
            return;

        Player player = event.getPlayer();
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);

        if (registry.isCustomBlock(block)) {
            CustomBlock handler = registry.getHandler(block);


            BlockBreakInstance instance = getInstanceByBlock(block);

            removeInstanceByBlock(event.getBlock());

            Location loc = event.getBlock().getLocation();
            BlockPos blockPos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            Util.sendPacketGlobal(new ClientboundBlockDestructionPacket(instance.getRandId(), blockPos, -1));

        }

    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event){
        Player player = event.getPlayer();
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);

        Block block = event.getBlock();
        if (block.getType() != Material.NOTE_BLOCK)
            return;

        event.setDropItems(false);

        if (registry.isCustomBlock(block)) {
            CustomBlock handler = registry.getHandler(block);
            handler.onBreak(plugin, event);
        }

    }

    @EventHandler
    public void blockExplode(BlockExplodeEvent event){
        Block block = event.getBlock();
        if (block.getType() != Material.NOTE_BLOCK)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void blockDestroy(BlockBurnEvent event){
        Block block = event.getBlock();
        if (block.getType() != Material.NOTE_BLOCK)
            return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void blockUpdateEvent(BlockPhysicsEvent event){
        Block block = event.getBlock().getLocation().add(0, 1, 0).getBlock();
        if(block.getType() == Material.NOTE_BLOCK)
            event.setCancelled(true);

        if (event.getBlock().getType() == Material.NOTE_BLOCK)
            event.setCancelled(true);

        event.getBlock().getState().update(true, false);
    }

    private void removeInstanceByBlock(Block block){
        BlockBreakInstance flag = null;
        for(BlockBreakInstance instance : breakMap) {
            if (instance.getBlock().getLocation().equals(block.getLocation())){
                flag = instance;
                break;
            }
        }
        breakMap.remove(flag);
    }

    private BlockBreakInstance getInstanceByBlock(Block block){

        BlockBreakInstance flag = null;
        for(BlockBreakInstance instance : breakMap) {
            if (instance.getBlock().getLocation().equals(block.getLocation())){
                flag = instance;
                break;
            }
        }
        return flag;
    }
}








































