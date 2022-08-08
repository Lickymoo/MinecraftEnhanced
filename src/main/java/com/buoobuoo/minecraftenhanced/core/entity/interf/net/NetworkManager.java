package com.buoobuoo.minecraftenhanced.core.entity.interf.net;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;

public class NetworkManager extends Connection {

    public NetworkManager(PacketFlow enumprotocoldirection) {

        super(enumprotocoldirection);
    }
}