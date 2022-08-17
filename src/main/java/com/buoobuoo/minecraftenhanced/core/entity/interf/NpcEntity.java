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

    default void onInteract(PlayerInteractNpcEvent event){

    }
}


























