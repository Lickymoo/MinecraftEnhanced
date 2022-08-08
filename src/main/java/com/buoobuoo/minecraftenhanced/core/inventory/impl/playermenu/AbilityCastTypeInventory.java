package com.buoobuoo.minecraftenhanced.core.inventory.impl.playermenu;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityCastType;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AbilityCastTypeInventory extends CustomInventory {
    private final int slot;

    public AbilityCastTypeInventory(MinecraftEnhanced plugin, Player player, int slot) {
        super(plugin, player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PLAYERMENU_ABILITY_CASTTYPE + UnicodeSpaceUtil.getNeg(169) + "&8Ability Cast Type", 36);

        this.slot = slot;

        this.addDefaultHandler(event -> {
            ItemStack item = event.getCurrentItem();
            if(item == null)
                return;

            String type = Util.getNBTString(plugin, item, "CAST_TYPE");
            AbilityCastType castType = AbilityCastType.valueOf(type);

            ProfileData profileData = plugin.getPlayerManager().getProfile(player);
            AbilityCastType[] types = Util.setArrSize(profileData.getAbilityCastTypes(), 4);

            types[slot] = castType;
            profileData.setAbilityCastTypes(types);

            Inventory i = new AbilityInventory(plugin, player).getInventory();
            player.openInventory(i);
        });

        this.addHandler(event -> {
            Inventory i = new AbilityInventory(plugin, player).getInventory();
            player.openInventory(i);
        }, 0);
    }

    @Override
    public Inventory getInventory(){
        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));

        ItemStack back = new ItemBuilder(MatRepo.INVISIBLE).name("&7Return to Ability Menu").create();
        inv.setItem(0, back);

        int index = 9;
        for(AbilityCastType castType : AbilityCastType.values()){
            if(castType == null) {
                index++;
                continue;
            }

            ItemStack item = new ItemBuilder(castType.getMat())
                    .setCustomModelData(castType.getCustomModelData())
                    .name(castType.getDisplayName())
                    .lore(castType.getDisplayLore())
                    .nbtString(plugin, "CAST_TYPE", castType.name()).create();
            inv.setItem(index++, item);
        }

        return inv;
    }
}






















