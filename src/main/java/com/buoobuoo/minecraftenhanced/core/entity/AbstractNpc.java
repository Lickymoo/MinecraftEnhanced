package com.buoobuoo.minecraftenhanced.core.entity;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.impl.util.NpcMirrorEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.NpcEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.net.NetworkManager;
import com.buoobuoo.minecraftenhanced.core.entity.interf.net.ServerGamePacketListener;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public abstract class AbstractNpc extends ServerPlayer implements NpcEntity {

    public AbstractNpc(Location loc) {
        super(((CraftWorld) loc.getWorld()).getHandle().getServer(), ((CraftWorld) loc.getWorld()).getHandle().getLevel(), new GameProfile(UUID.randomUUID(), ""), null);

        MinecraftServer minecraftServer = ((CraftWorld) loc.getWorld()).getHandle().getServer();
        this.connection = new ServerGamePacketListener(minecraftServer, new NetworkManager(PacketFlow.CLIENTBOUND), this);

        this.getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0xFF);
    }

    @Override
    public void entityTick() {
        NpcEntity.super.entityTick();

        NpcMirrorEntity mirrorEntity = getChild("MIRROR_ENTITY");
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

    private void hideScoreboard(MinecraftEnhanced plugin){
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("");

        if(team == null)
            team = scoreboard.registerNewTeam("");
        team.addEntry("");
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    @Override
    public void spawnEntity(MinecraftEnhanced plugin, Location loc){
        NpcEntity.super.spawnEntity(plugin, loc);
        NpcMirrorEntity mirrorEntity = (NpcMirrorEntity) plugin.getEntityManager().spawnEntity(NpcMirrorEntity.class, loc);
        addChild("MIRROR_ENTITY", mirrorEntity);

        hideScoreboard(plugin);
        show();
    }

    public void show() {
        hiddenPlayers.putIfAbsent(this, new HashSet<>());
        Set<UUID> hidden = hiddenPlayers.get(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            //if hiddenPlayers = players to show to
            if (getInvertHide() && !hidden.contains(uuid)) continue;
            //if hiddenPlayers = players to hide from
            if (!getInvertHide() && hidden.contains(uuid)) continue;

            Util.sendPacket(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, (ServerPlayer) asEntity()), player);
            Util.sendPacket(new ClientboundAddPlayerPacket((ServerPlayer) asEntity()), player);
            Util.sendPacket(new ClientboundSetEntityDataPacket(asEntity().getBukkitEntity().getEntityId(), asEntity().getEntityData(), true), player);
        }
    }

    @Override
    public void onDeath(){
        NpcEntity.super.onDeath();
        NpcMirrorEntity mirrorEntity = getChild("MIRROR_ENTITY");
        MinecraftEnhanced.getInstance().getEntityManager().removeEntity(mirrorEntity);
    }

    @Override
    public PathfinderMob getPathfinderMob(){
        NpcMirrorEntity mirrorEntity = getChild("MIRROR_ENTITY");
        return mirrorEntity.getPathfinderMob();
    }

}