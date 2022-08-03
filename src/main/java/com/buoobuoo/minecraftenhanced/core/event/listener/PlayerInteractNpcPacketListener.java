package com.buoobuoo.minecraftenhanced.core.event.listener;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import com.buoobuoo.minecraftenhanced.core.entity.npc.NpcInstance;
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
                NpcInstance handler = pluginE.getNpcManager().getInstanceByEntityID(id);



                Bukkit.getScheduler().runTask(plugin, () -> {
                    PlayerInteractNpcEvent sendEvent = new PlayerInteractNpcEvent(player, handler);
                    Bukkit.getServer().getPluginManager().callEvent(sendEvent);
                });
            }
        });
    }
}
