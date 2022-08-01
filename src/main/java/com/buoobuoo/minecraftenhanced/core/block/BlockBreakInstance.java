package com.buoobuoo.minecraftenhanced.core.block;

import com.buoobuoo.minecraftenhanced.core.util.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BlockBreakInstance {
    private Block block;
    private CustomBlock customBlock;
    private int currentTick;
    private int maxTick;
    private ItemStack item;
    private UUID playerUUID;

    private final int randId = Util.randomInt(0, Integer.MAX_VALUE);
}
