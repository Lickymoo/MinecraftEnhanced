package com.buoobuoo.minecraftenhanced.core.entity.interf.net;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ServerGamePacketListener extends ServerGamePacketListenerImpl {
    public ServerGamePacketListener(MinecraftServer minecraftserver, Connection networkmanager, ServerPlayer entityplayer) {
        super(minecraftserver, networkmanager, entityplayer);
    }

    @Override
    public void send(Packet<?> packet) {
        // Empty as we never want to try an send packets to a fake player.
    }
}
