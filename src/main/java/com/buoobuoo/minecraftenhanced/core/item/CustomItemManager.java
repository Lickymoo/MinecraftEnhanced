package com.buoobuoo.minecraftenhanced.core.item;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.damage.DamageManager;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateTickEvent;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.*;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.MagicWeapon;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.RangedWeapon;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

    public ItemStack getItem(ProfileData profileData, CustomItems item){
        return getItem(profileData, item.getHandler());
    }

    public ItemStack getItem(ProfileData profileData, CustomItem item){

        ItemBuilder ib = new ItemBuilder(item.getMaterial());
        ib.name(item.getRarity().getColor() + item.getDisplayName());
        item.onCreate(plugin,  ib);

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

        ItemStack itemStack = ib.create();

        if(profileData != null)
            itemStack = item.update(plugin, profileData, itemStack);
        return itemStack;
    }

    @EventHandler
    public void onRangedWeaponHit(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        ItemStack itemStack = event.getItem();

        if (itemStack == null)
            return;

        CustomItemManager customItemManager = plugin.getCustomItemManager();
        CustomItem handler = customItemManager.getRegistry().getHandler(itemStack);
        if(handler == null)
            return;
        if(!(handler instanceof RangedWeapon rangedWeapon))
            return;

        Player player = event.getPlayer();
        if(!customItemManager.canAttack(player))
            return;

        Vector playerDirection = player.getLocation().getDirection();
        Projectile proj = player.launchProjectile(rangedWeapon.projectileType(), playerDirection);
        proj.setShooter(player);
    }

    @EventHandler
    public void onMagicWeaponHit(PlayerInteractEvent event){
        if(event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        ItemStack itemStack = event.getItem();

        if(itemStack == null)
            return;

        CustomItemManager customItemManager = plugin.getCustomItemManager();
        CustomItem handler = customItemManager.getRegistry().getHandler(itemStack);
        if(handler == null)
            return;
        if(!(handler instanceof MagicWeapon magicWeapon))
            return;
        Player player = event.getPlayer();
        if(!customItemManager.canAttack(player))
            return;

        float range = 7;

        Location loc = player.getLocation();
        double time = 0.0D;
        Vector dir = loc.getDirection();

        for (float i = 0; i < range * 4; i+= 1) {
            time += 0.25D;
            double x = dir.getX() * time;
            double y = dir.getY() * time + 1.25D;
            double z = dir.getZ() * time;
            loc.add(x, y, z);
            magicWeapon.spawnParticle(loc);
            loc.subtract(x, y, z);
        }

        Entity entity = Util.getTarget(player, range);
        if(entity != null) {
            DamageManager damageManager = plugin.getDamageManager();

            if(entity instanceof Player){
                damageManager.handleDamageP2P(player, (Player)entity);
            }else {
                CustomEntity entityHandler = plugin.getEntityManager().getHandlerByEntity(entity);
                if (entityHandler == null)
                    return;

                EntityDamageByEntityEvent ev = new EntityDamageByEntityEvent(player, entity, EntityDamageEvent.DamageCause.CUSTOM, 1);
                Bukkit.getPluginManager().callEvent(ev);
            }
        }
    }

    private ConcurrentHashMap<UUID, Integer> cooldownList = new ConcurrentHashMap<>();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cooldownHit(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if(itemStack == null)
            return;

        CustomItem handler = plugin.getCustomItemManager().getRegistry().getHandler(itemStack);
        if(handler == null)
            return;
        if(!(handler instanceof Cooldown))
            return;

        if(!canAttack(player)){
            return;
        }

        Cooldown cooldown = (Cooldown)handler;

        int cooldownTicks = cooldown.cooldownTicks();

        Item item = Item.byId(itemStack.getType().ordinal());
        ClientboundCooldownPacket packet = new ClientboundCooldownPacket(item, cooldownTicks);
        Util.sendPacket(packet, player);

        cooldownList.put(player.getUniqueId(), cooldownTicks);
    }

    public boolean canAttack(Player player){
        if(cooldownList.getOrDefault(player.getUniqueId(), 0) >= 1){
            return false;
        }
        return true;
    }

    @EventHandler
    public void onQuestItemDrop(PlayerDropItemEvent event){
        ItemStack item = event.getItemDrop().getItemStack();
        CustomItem handler = getRegistry().getHandler(item);

        if(!(handler instanceof QuestItem))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void tickCooldown(UpdateTickEvent event){
        for(Map.Entry<UUID, Integer> entry : cooldownList.entrySet()){
            int ticks = entry.getValue();
            ticks -= 1;

            if(ticks <= 0){
                cooldownList.remove(entry.getKey());
                continue;
            }

            cooldownList.put(entry.getKey(), ticks);
        }
    }

    public void updateHeldItem(ItemStack item){
        if(item == null)
            return;

        CustomItem handler = registry.getHandler(item);
        if(!(handler instanceof AlternateHeldItem alternateHeldItem))
            return;

        MatRepo swapout = alternateHeldItem.swapoutItem();

        ItemMeta meta = item.getItemMeta();;
        meta.setCustomModelData(swapout.getCustomModelData());
        item.setType(swapout.getMat());
        item.setItemMeta(meta);
    }

    public void revertHeldItem(ItemStack item){
        if(item == null)
            return;

        CustomItem handler = registry.getHandler(item);
        if(!(handler instanceof AlternateHeldItem alternateHeldItem))
            return;

        ItemMeta meta = item.getItemMeta();;
        meta.setCustomModelData(handler.getCustomModelData());
        item.setType(handler.getMaterial());
        item.setItemMeta(meta);
    }

    public void updateHeldItems(Player player){
        for(ItemStack item : player.getInventory()){
            revertHeldItem(item);
        }
        updateHeldItem(player.getInventory().getItemInMainHand());
    }

    @EventHandler
    public void changeSlot(PlayerItemHeldEvent event){
        int slot = event.getNewSlot();
        ItemStack item = event.getPlayer().getInventory().getItem(slot);
        updateHeldItem(item);
        event.getPlayer().getInventory().setItem(slot, item);

        int oldSlot = event.getPreviousSlot();
        item = event.getPlayer().getInventory().getItem(oldSlot);
        revertHeldItem(item);
        event.getPlayer().getInventory().setItem(oldSlot, item);
    }

    @EventHandler
    public void cancelSwap(PlayerSwapHandItemsEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void moveSwapItem(InventoryClickEvent event){
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Player player = (Player) event.getWhoClicked();
            updateHeldItems(player);
        }, 1);
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








































