package com.buoobuoo.minecraftenhanced.core.inventory.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TestInventory extends CustomInventory {
    public TestInventory(MinecraftEnhanced plugin, Player player){
        super(plugin, player, UnicodeSpaceUtil.getNeg(8) + "&r&f\uF004", 27);
        for(int i = 0; i < 9; i++){
            int finalI = i;
            slotMap.put(i, e -> {
                System.out.println(finalI);
            });
        }
    }

    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(this, size, Util.formatColour(title));
    }
}
