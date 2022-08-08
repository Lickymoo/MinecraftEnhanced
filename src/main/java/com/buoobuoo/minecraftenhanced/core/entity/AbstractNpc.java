package com.buoobuoo.minecraftenhanced.core.entity;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.impl.NpcMirrorEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.net.NetworkManager;
import com.buoobuoo.minecraftenhanced.core.entity.interf.NpcEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.net.ServerGamePacketListener;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class AbstractNpc extends ServerPlayer implements NpcEntity {

    //use bukkit entity for movement
    private CustomEntity mirrorEntity;
    private ConcurrentLinkedDeque<UUID> viewers = new ConcurrentLinkedDeque<>();

    public AbstractNpc(Location loc) {
        super(((CraftWorld) loc.getWorld()).getHandle().getServer(), ((CraftWorld) loc.getWorld()).getHandle().getLevel(), new GameProfile(UUID.randomUUID(), Util.randomString()), null);

        MinecraftServer minecraftServer = ((CraftWorld) loc.getWorld()).getHandle().getServer();
        this.connection = new ServerGamePacketListener(minecraftServer, new NetworkManager(PacketFlow.CLIENTBOUND), this);

        System.out.println(this.uuid);
        this.getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0xFF);
    }

    @Override
    public void entityTick() {
        NpcEntity.super.entityTick();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!viewers.contains(player.getUniqueId())) {
                viewers.add(player.getUniqueId());
                show(player);
            }
        }

        viewers.removeIf(uuid -> Bukkit.getPlayer(uuid) == null);

        Location loc = mirrorEntity.asEntity().getBukkitEntity().getLocation();

        //Look at player
        if (loc.distance(asEntity().getBukkitEntity().getLocation()) == 0) {
            for(Entity entity : loc.getWorld().getNearbyEntities(loc, 10, 10 ,10)) {
                if (!(entity instanceof Player))
                    continue;
                loc.setDirection(entity.getLocation().subtract(loc).toVector());
            }
        }

        double mirrorX = loc.getX();
        double mirrorY = loc.getY();
        double mirrorZ = loc.getZ();

        byte yaw = (byte) ((loc.getYaw() % 360) * 256 / 360);
        byte pitch = (byte) ((loc.getPitch() % 360) * 256 / 360);

        asEntity().moveTo(mirrorX, mirrorY, mirrorZ, yaw, pitch);
        Util.sendPacketGlobal(new ClientboundRotateHeadPacket(this, yaw));
        Util.sendPacketGlobal(new ClientboundTeleportEntityPacket(asEntity()));
    }

    @Override
    public void spawnEntity(MinecraftEnhanced plugin, Location loc){
        NpcEntity.super.spawnEntity(plugin, loc);
        mirrorEntity = plugin.getEntityManager().spawnEntity(NpcMirrorEntity.class, loc);
    }

    @Override
    public void onDeath(){
        NpcEntity.super.onDeath();
        mirrorEntity.asEntity().remove(RemovalReason.DISCARDED);
    }

    @Override
    public PathfinderMob getPathfinderMob(){
        return mirrorEntity.getPathfinderMob();
    }
}