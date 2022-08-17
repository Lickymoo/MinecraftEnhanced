package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateTickEvent;
import com.buoobuoo.minecraftenhanced.core.util.ByteBufData;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class ServerBrandListener implements Listener {

    private final MinecraftEnhanced plugin;
    private static Field playerChannelsField;
    String channel = MinecraftEnhanced.BRAND_CHANNEL;

    public ServerBrandListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (playerChannelsField == null) {
            try {
                playerChannelsField = event.getPlayer().getClass().getDeclaredField("channels");
                playerChannelsField.setAccessible(true);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        try {
            Set<String> channels = (Set<String>) playerChannelsField.get(event.getPlayer());
            channels.add(channel);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        updateBrand(event.getPlayer());
    }

    private void updateBrand(Player player) {
        ByteBuf byteBuf = Unpooled.buffer();
        ByteBufData.writeString(Util.formatColour("&7Buby is so cool&r"), byteBuf);
        player.sendPluginMessage(plugin, channel, ByteBufData.toArray(byteBuf));
        byteBuf.release();
    }
}
