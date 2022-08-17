package com.buoobuoo.minecraftenhanced.core.inventory.impl.playermenu;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.ability.Ability;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityCastType;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityManager;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.item.impl.AbilityGemItem;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerMenuMainInventory extends CustomInventory {
    private final Player target;
    public PlayerMenuMainInventory(MinecraftEnhanced plugin, Player target, Player player) {
        super(plugin, player, "", 36);

        this.target = target;

        this.addDefaultHandler( event -> {
            Inventory inv = new AbilityInventory(plugin, player).getInventory();
            player.openInventory(inv);
        });
    }

    @Override
    public Inventory getInventory(){
        StringBuilder title = new StringBuilder(UnicodeSpaceUtil.getNeg(8) + "&r&f");
        title.append(CharRepo.UI_INVENTORY_PLAYERMENU_SELF_MAIN);
        title.append(UnicodeSpaceUtil.getNeg(177));

        PlayerInventory targetInventory = target.getInventory();
        ItemStack helmet = targetInventory.getHelmet();
        ItemStack chestplate = targetInventory.getChestplate();
        ItemStack leggings = targetInventory.getLeggings();
        ItemStack boots = targetInventory.getBoots();

        //if null add icon
        if(helmet == null) {
            title.append(CharRepo.UI_INVENTORY_PLAYERMENU_HELMET_ICON);
            title.append(UnicodeSpaceUtil.getNeg(177));
        }
        if(chestplate == null){
            title.append(CharRepo.UI_INVENTORY_PLAYERMENU_CHESTPLATE_ICON);
            title.append(UnicodeSpaceUtil.getNeg(177));
        }
        if(leggings == null){
            title.append(CharRepo.UI_INVENTORY_PLAYERMENU_LEGGINGS_ICON);
            title.append(UnicodeSpaceUtil.getNeg(177));
        }
        if(boots == null) {
            title.append(CharRepo.UI_INVENTORY_PLAYERMENU_BOOTS_ICON);
            title.append(UnicodeSpaceUtil.getNeg(177));
        }



        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title.toString()));
        inv.setItem(8, helmet);
        inv.setItem(17, chestplate);
        inv.setItem(26, leggings);
        inv.setItem(35, boots);

        return inv;
    }
}
