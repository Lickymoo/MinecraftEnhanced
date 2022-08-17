package com.buoobuoo.minecraftenhanced.core.vfx.cinematic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.impl.util.EmptyEntity;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Pair;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.util.EntityHider;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpectatorManager implements Listener {
    private final MinecraftEnhanced plugin;

    Map<UUID, EmptyEntity> entityMap = new HashMap<>();
    Map<UUID, Pair<Location, ItemStack>> lastLoc = new HashMap<>();
    EntityHider entityHider;

    public SpectatorManager(MinecraftEnhanced plugin) {
        this.plugin = plugin;
        entityHider = new EntityHider(plugin, EntityHider.Policy.BLACKLIST);
    }

    public EmptyEntity getStandEntity(Location loc) {
        return (EmptyEntity)plugin.getEntityManager().spawnEntity(EmptyEntity.class, loc);

    }

    public EmptyEntity getStand(Player player, Location loc) {
        entityMap.putIfAbsent(player.getUniqueId(), getStandEntity(loc));
        return entityMap.get(player.getUniqueId());
    }

    public EmptyEntity viewLoc(Player player, Location loc) {
        if(entityMap.containsKey(player.getUniqueId())) return null;
        lastLoc.put(player.getUniqueId(), Pair.of(player.getLocation(),player.getInventory().getHelmet()));
        EmptyEntity stand = getStand(player, loc);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(stand.asEntity().getBukkitEntity());
            player.getInventory().setHelmet(new ItemBuilder(Material.CARVED_PUMPKIN).name("There was no better way lol").create());
        }, 1);
        return stand;
    }

    public EmptyEntity viewLocNoVignette(Player player, Location loc) {
        if(entityMap.containsKey(player.getUniqueId())) return null;
        lastLoc.put(player.getUniqueId(), Pair.of(player.getLocation(),player.getInventory().getHelmet()));
        EmptyEntity stand = getStand(player, loc);
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(stand.asEntity().getBukkitEntity());

        return stand;
    }

    public void stopSpectatorMode(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(null);
        player.setGameMode(GameMode.SURVIVAL);

        if(!entityMap.containsKey(player.getUniqueId())) return;
        player.teleport(lastLoc.get(player.getUniqueId()).getLeft());
        player.getInventory().setHelmet(lastLoc.get(player.getUniqueId()).getRight());
        lastLoc.remove(player.getUniqueId());
        EmptyEntity entity = entityMap.get(player.getUniqueId());
        plugin.getEntityManager().removeEntity(entity);
        entityMap.remove(player.getUniqueId());
    }

    @EventHandler
    public void playerItemHeldEvent(PlayerItemHeldEvent e) {
        if(e.getPlayer().getGameMode() == GameMode.SPECTATOR)
            e.getPlayer().getInventory().setHeldItemSlot(8);
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent e) {
        if(entityMap.containsKey(e.getPlayer().getUniqueId())){
            stopSpectatorMode(e.getPlayer());
        }
    }

    public static void changeAllBlocks(Player player, BlockData mat, Location... locs) {
        for(Location loc : locs) {
            player.sendBlockChange(loc, mat);
        }
    }

}






