package com.buoobuoo.minecraftenhanced.core.event.listener;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.AnvilRenameEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AnvilRenamePacketListener {
    private final MinecraftEnhanced pluginE;

    public AnvilRenamePacketListener(MinecraftEnhanced pluginE){
        this.pluginE = pluginE;
        pluginE.getProtocolManager().addPacketListener(new PacketAdapter(
                pluginE,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.ITEM_NAME
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                Inventory inv = player.getOpenInventory().getTopInventory();
                CustomInventory handler = pluginE.getCustomInventoryManager().getRegistry().getHandler(inv);

                PacketContainer packet = event.getPacket();
                String name = packet.getStrings().getValues().get(0);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    AnvilRenameEvent sendEvent = new AnvilRenameEvent(player, handler, name);
                    Bukkit.getServer().getPluginManager().callEvent(sendEvent);
                });
            }
        });
    }
}
