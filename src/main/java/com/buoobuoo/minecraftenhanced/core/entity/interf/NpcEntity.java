package com.buoobuoo.minecraftenhanced.core.entity.interf;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public interface NpcEntity extends CustomEntity {

    String textureSignature();
    String textureBase64();

    @Override
    default void spawnEntity(MinecraftEnhanced plugin, Location loc){
        Entity ent = asEntity();
        ((ServerPlayer)this).getGameProfile().getProperties().put("textures", new Property("textures", textureBase64(), textureSignature()));

        ent.setPos(loc.getX(), loc.getY(), loc.getZ());

        PersistentDataContainer pdc = ent.getBukkitEntity().getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, "ENT_ID"), PersistentDataType.STRING, entityID());
        pdc.set(new NamespacedKey(plugin, "HEALTH"), PersistentDataType.DOUBLE, maxHealth());
        pdc.set(new NamespacedKey(plugin, "DAMAGE"), PersistentDataType.DOUBLE, damage());

        onSpawn();

        plugin.getEntityManager().registerEntities(this);
    }

    default void onInteract(PlayerInteractNpcEvent event){

    }

    @Override
    default void destroyEntity(){
        isDestroyed.put(this, true);
        getChildren().forEach(CustomEntity::destroyEntity);
        Util.sendPacketGlobal(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, (ServerPlayer) asEntity()));
        Util.sendPacketGlobal(new ClientboundRemoveEntitiesPacket(asEntity().getBukkitEntity().getEntityId()));
    }
}


























