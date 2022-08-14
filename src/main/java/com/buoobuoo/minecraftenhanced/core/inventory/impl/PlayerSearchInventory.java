package com.buoobuoo.minecraftenhanced.core.inventory.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.AnvilRenameEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerSearchInventory extends CustomInventory {
    private final Consumer<Player> consumer;
    private Collection<Player> playerList;

    public PlayerSearchInventory(MinecraftEnhanced plugin, Player player, Consumer<Player> consumer){
        this(plugin, player, new ArrayList<>(Bukkit.getOnlinePlayers()), consumer);
    }

    public PlayerSearchInventory(MinecraftEnhanced plugin, Player player, Collection<Player> playerSet, Consumer<Player> consumer){
        super(plugin, player, UnicodeSpaceUtil.getNeg(60)
                + "&r&f" + CharRepo.UI_INVENTORY_PLAYER_SEARCH_OVERLAY
                + UnicodeSpaceUtil.getNeg(117)
                +"&8Enter player name", 27);

        this.consumer = consumer;
        this.playerList = playerSet;
        slotMap.put(2, event -> {
            if(event.getInventory().getItem(2) == null){
                return;
            }
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);

            ItemStack item = event.getInventory().getItem(2);
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            String playerUUID = pdc.get(new NamespacedKey(plugin, "PLAYER_SEL"), PersistentDataType.STRING);
            UUID uuid = UUID.fromString(playerUUID);

            consumer.accept(Bukkit.getPlayer(uuid));
            playerList = null; //clear mem

        });
    }

    private Player getPlayer(String name){
        for(Player player : playerList){
            if(player.getName().equalsIgnoreCase(name))
                return player;
        }
        return null;
    }

    @Override
    public Inventory getInventory() {
        ItemStack item = new ItemBuilder(Material.PLAYER_HEAD).setCustomModelData(10)
                .skullTexture(
                        "eyJ0aW1lc3RhbXAiOjE1ODc4MjU0NzgwNDcsInByb2ZpbGVJZCI6ImUzYjQ0NWM4NDdmNTQ4ZmI4YzhmYTNmMWY3ZWZiYThlIiwicHJvZmlsZU5hbWUiOiJNaW5pRGlnZ2VyVGVzdCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E1ODg4YWEyZDdlMTk5MTczYmEzN2NhNzVjNjhkZTdkN2Y4NjJiMzRhMTNiZTMyNDViZTQ0N2UyZjIyYjI3ZSJ9fX0=",
                        "Yt6VmTAUTbpfGQoFneECtoYcbu7jcARAwZu2LYWv3Yf1MJGXv6Bi3i7Pl9P8y1ShB7V1Q2HyA1bce502x1naOKJPzzMJ0jKZfEAKXnzaFop9t9hXzgOq7PaIAM6fsapymYhkkulRIxnJdMrMb2PLRYfo9qiBJG+IEbdj8MTSvWJO10xm7GtpSMmA2Xd0vg5205hsj0OxSdgxf1uuWPyRaXpPZYDUU05/faRixDKti86hlkBs/v0rttU65r1UghkftfjK0sJoPpk9hABvkw4OjXVFb63wcb27KPhIiSHZzTooSxjGNDniauCsF8Je+fhhMebpXeba1R2lZPLhkHwazNgZmTCKbV1M/a8BDHN24HH9okJpQOR9SPCPOJrNbK+LTPsrR06agj+H/yvYq0ZMJTF6IE6C3KJqntPJF1NQvJM0/YegPPtzpbT/7O1cd4JBCVmguhadOFYvrxqCKHcmaYdkyMJtnGub/5sCjJAG7fZadACftwLnmdBZoQRcNKQMubpdUjuzF8g6C03MiZkeNBUgqkfVjXi7DqpmB0ZvTttp34vy7EIBCo3Hfj15779nGs8SoTw9V2zZc+LgiVPjWF6tffjWkgzLq8K2Cndu6RDlWGJWmrztN/X9lIiLdn8GEfSSGY983n0C91x8mkpOKSfAWPnSZd7NuHU5GaoMvyE="
                )
                .name(" ").create();

        Inventory inv = Bukkit.createInventory(this, InventoryType.ANVIL, Util.formatColour(title));
        inv.setItem(0, item);

        ItemStack invis = new ItemBuilder(MatRepo.INVISIBLE).name(" ").create();
        inv.setItem(1,invis);

        return inv;
    }

    @EventHandler
    public void onRename(AnvilRenameEvent event){
        if(event.getHandler() != this)
            return;

        String name = event.getName().trim();
        Player player = getPlayer(name);
        Inventory inv = event.getPlayer().getOpenInventory().getTopInventory();

        String curName = inv.getItem(0).getItemMeta().getDisplayName();
        inv.setItem(2, null);
        if(curName.equals("Player Head") || name.isBlank()){
            name = " ";
        }
        if(curName.equals(name) && player == null){
            return;
        }

        if(player != null) {
            ItemStack head = new ItemBuilder(Material.PLAYER_HEAD).setCustomModelData(10)
                    .skullOwner(player)
                    .name(name).create();
            inv.setItem(0, head);

            ItemStack accept = new ItemBuilder(MatRepo.GREEN_TICK).name(Util.formatColour("&a&lSelect Player")).create();
            ItemMeta meta = accept.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(new NamespacedKey(plugin, "PLAYER_SEL"), PersistentDataType.STRING, player.getUniqueId().toString());
            accept.setItemMeta(meta);

            inv.setItem(2,accept);
        }else{
            ItemStack head = new ItemBuilder(Material.PLAYER_HEAD).setCustomModelData(10)
                    .skullTexture(
                            "eyJ0aW1lc3RhbXAiOjE1ODc4MjU0NzgwNDcsInByb2ZpbGVJZCI6ImUzYjQ0NWM4NDdmNTQ4ZmI4YzhmYTNmMWY3ZWZiYThlIiwicHJvZmlsZU5hbWUiOiJNaW5pRGlnZ2VyVGVzdCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E1ODg4YWEyZDdlMTk5MTczYmEzN2NhNzVjNjhkZTdkN2Y4NjJiMzRhMTNiZTMyNDViZTQ0N2UyZjIyYjI3ZSJ9fX0=",
                            "Yt6VmTAUTbpfGQoFneECtoYcbu7jcARAwZu2LYWv3Yf1MJGXv6Bi3i7Pl9P8y1ShB7V1Q2HyA1bce502x1naOKJPzzMJ0jKZfEAKXnzaFop9t9hXzgOq7PaIAM6fsapymYhkkulRIxnJdMrMb2PLRYfo9qiBJG+IEbdj8MTSvWJO10xm7GtpSMmA2Xd0vg5205hsj0OxSdgxf1uuWPyRaXpPZYDUU05/faRixDKti86hlkBs/v0rttU65r1UghkftfjK0sJoPpk9hABvkw4OjXVFb63wcb27KPhIiSHZzTooSxjGNDniauCsF8Je+fhhMebpXeba1R2lZPLhkHwazNgZmTCKbV1M/a8BDHN24HH9okJpQOR9SPCPOJrNbK+LTPsrR06agj+H/yvYq0ZMJTF6IE6C3KJqntPJF1NQvJM0/YegPPtzpbT/7O1cd4JBCVmguhadOFYvrxqCKHcmaYdkyMJtnGub/5sCjJAG7fZadACftwLnmdBZoQRcNKQMubpdUjuzF8g6C03MiZkeNBUgqkfVjXi7DqpmB0ZvTttp34vy7EIBCo3Hfj15779nGs8SoTw9V2zZc+LgiVPjWF6tffjWkgzLq8K2Cndu6RDlWGJWmrztN/X9lIiLdn8GEfSSGY983n0C91x8mkpOKSfAWPnSZd7NuHU5GaoMvyE="
                            )
                    .name(name).create();
            inv.setItem(0, head);

            inv.setItem(2, null);
        }

    }
}
