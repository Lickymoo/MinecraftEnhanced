package com.buoobuoo.minecraftenhanced.core.event.listener;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.NpcEntity;
import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerInteractNpcPacketListener {
    private final MinecraftEnhanced pluginE;

    public PlayerInteractNpcPacketListener(MinecraftEnhanced pluginE){
        this.pluginE = pluginE;
        pluginE.getProtocolManager().addPacketListener(new PacketAdapter(
                pluginE,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.USE_ENTITY
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();
                if(action != EnumWrappers.EntityUseAction.INTERACT)
                    return;

                EnumWrappers.Hand hand = packet.getEnumEntityUseActions().read(0).getHand();
                if(hand != EnumWrappers.Hand.MAIN_HAND)
                    return;

                Player player = event.getPlayer();
                int id = packet.getIntegers().read(0) ;

                CustomEntity entityHandler = pluginE.getEntityManager().getHandlerByID(id);
                if(!(entityHandler instanceof NpcEntity))
                    return;

                NpcEntity handler = (NpcEntity) entityHandler;



                Bukkit.getScheduler().runTask(plugin, () -> {
                    PlayerInteractNpcEvent sendEvent = new PlayerInteractNpcEvent(player, handler);
                    Bukkit.getServer().getPluginManager().callEvent(sendEvent);
                });
            }
        });
    }
}


























