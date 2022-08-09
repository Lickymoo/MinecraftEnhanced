package com.buoobuoo.minecraftenhanced.core.entity;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class AbstractSingularViewerNpc extends AbstractNpc{

    //use bukkit entity for movement
    protected UUID viewer;

    public AbstractSingularViewerNpc(Location loc, UUID viewer) {
        super(loc);
        this.viewer = viewer;
    }

    @Override
    public void show(Player player){
        if(player.getUniqueId() != viewer)
            return;

        Util.sendPacket(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, (ServerPlayer) asEntity()), player);
        Util.sendPacket(new ClientboundAddPlayerPacket((ServerPlayer)asEntity()), player);
        Util.sendPacket(new ClientboundSetEntityDataPacket(asEntity().getBukkitEntity().getEntityId(), asEntity().getEntityData(), true), player);
    }

    @Override
    public void onDestroy(){
        Player player = Bukkit.getPlayer(viewer);
        Util.sendPacket(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, (ServerPlayer) asEntity()), player);
        Util.sendPacket(new ClientboundRemoveEntitiesPacket(asEntity().getBukkitEntity().getEntityId()), player);
        MinecraftEnhanced.getInstance().getEntityManager().removeEntity(mirrorEntity);
    }
}