package com.buoobuoo.minecraftenhanced.core.cinematic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.cinematic.util.EntityHider;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Pair;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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

    Map<UUID, Entity> entityMap = new HashMap<>();
    Map<UUID, Pair<Location, ItemStack>> lastLoc = new HashMap<>();
    EntityHider entityHider;

    public SpectatorManager(MinecraftEnhanced plugin) {
        this.plugin = plugin;
        entityHider = new EntityHider(plugin, EntityHider.Policy.BLACKLIST);
    }

    public Entity getStandEntity(Location loc) {
        Entity e = loc.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
        AreaEffectCloud cloud = (AreaEffectCloud)e;
        cloud.setRadius(0);
        cloud.setGravity(false);
        cloud.setCustomNameVisible(false);
        cloud.setCustomNameVisible(true);
        cloud.setInvulnerable(true);
        cloud.setDuration(2147483646/2);
        cloud.setRadiusPerTick(0);
        cloud.setParticle(Particle.BLOCK_CRACK, Material.AIR.createBlockData());
        cloud.setDurationOnUse(0);
        return e;

    }

    public Entity getStand(Player player, Location loc) {
        if(entityMap.containsKey(player.getUniqueId())) {
            return entityMap.get(player.getUniqueId());
        }else {
            Entity entity = getStandEntity(loc);
            entityMap.put(player.getUniqueId(), entity);
            return entity;
        }
    }

    public Entity viewLoc(Player player, Location loc) {
        if(entityMap.containsKey(player.getUniqueId())) return null;
        lastLoc.put(player.getUniqueId(), Pair.of(player.getLocation(),player.getInventory().getHelmet()));
        Entity stand = getStand(player, loc);
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(stand);
        player.getInventory().setHelmet(new ItemBuilder(Material.CARVED_PUMPKIN).coloredName("There was no better way lol").create());

        return stand;
    }

    public Entity viewLocNoVignette(Player player, Location loc) {
        if(entityMap.containsKey(player.getUniqueId())) return null;
        lastLoc.put(player.getUniqueId(), Pair.of(player.getLocation(),player.getInventory().getHelmet()));
        Entity stand = getStand(player, loc);
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(stand);

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
        entityMap.get(player.getUniqueId()).remove();
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






