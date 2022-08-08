package com.buoobuoo.minecraftenhanced.core.inventory.impl.playermenu;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.ability.Ability;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityCastType;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityManager;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventory;
import com.buoobuoo.minecraftenhanced.core.item.AbilityGemItem;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AbilityInventory extends CustomInventory {
    public AbilityInventory(MinecraftEnhanced plugin, Player player) {
        super(plugin, player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PLAYERMENU_ABILITIES + UnicodeSpaceUtil.getNeg(169) + "&8Ability Menu", 36);

        this.addHandler(event -> {
            ItemStack item = event.getCurrentItem();
            if(item == null)
                return;

            if(player.getInventory().firstEmpty() == -1){
                player.sendMessage(Util.formatColour("&7Cannot unsocket ability gem, inventory is full."));
                return;
            }

            String abilityID = Util.getNBTString(plugin, item, "ABILITY_ID");

            ProfileData profileData = plugin.getPlayerManager().getProfile(player);
            String[] playerAbilities = Util.removeOccurrences(profileData.getAbilityIDs(), abilityID);
            profileData.setAbilityIDs(playerAbilities);
            Ability ability = plugin.getAbilityManager().getAbilityByID(abilityID);

            //Default to a type with no effectiveness decrease
            item = new ItemBuilder(item).clearLore().lore(ability.getLore(AbilityCastType.NONE)).create();

            player.getInventory().addItem(item);

            int slot = (event.getSlot()-10)/2;

            AbilityCastType[] types = profileData.getAbilityCastTypes();
            types[slot] = null;
            profileData.setAbilityCastTypes(types);

            Inventory inv = new AbilityInventory(plugin, player).getInventory();
            player.openInventory(inv);
        }, 10, 12, 14, 16);


        this.addHandler(event -> {
            Inventory inv = event.getClickedInventory();
            if(inv.getItem(event.getSlot() - 18) == null)
                return;

            int slot = (event.getSlot()-28)/2;

            Inventory castInv = new AbilityCastTypeInventory(plugin, player, slot).getInventory();
            player.openInventory(castInv);
        }, 28, 30, 32, 34);

        this.addHandler(event -> {
            Inventory i = new PlayerMenuMainInventory(plugin, player).getInventory();
            player.openInventory(i);
        }, 0);
    }

    @Override
    public Inventory getInventory(){
        Inventory inv = Bukkit.createInventory(this, size, Util.formatColour(title));
        AbilityManager abilityManager = plugin.getAbilityManager();
        CustomItemManager customItemManager = plugin.getCustomItemManager();

        ProfileData profileData = plugin.getPlayerManager().getProfile(player);


        int abilityIndex = 10;
        int castTypeIndex = 28;
        int index = 0;

        AbilityCastType[] types = Util.setArrSize(profileData.getAbilityCastTypes(), 4);

        for(Ability ability : abilityManager.getPlayerAbilities(profileData)){
            AbilityCastType type = types[index];
            if(ability != null) {
                AbilityGemItem gemItem = new AbilityGemItem(ability);
                ItemStack gem = customItemManager.getItem(profileData, gemItem);

                if(type != null)
                    gem = new ItemBuilder(gem).name(ability.getName()).clearLore().lore(ability.getLore(type)).create();

                inv.setItem(abilityIndex, gem);
            }
            if(type != null) {
                ItemStack castTypeItem = new ItemBuilder(type.getMat())
                        .setCustomModelData(type.getCustomModelData())
                        .name(type.getDisplayName())
                        .lore(type.getDisplayLore())
                        .create();

                inv.setItem(castTypeIndex, castTypeItem);

                if(type == AbilityCastType.NONE)
                    inv.setItem(castTypeIndex, null);
            }

            abilityIndex+=2;
            castTypeIndex+=2;
            index++;
        }

        ItemStack back = new ItemBuilder(MatRepo.INVISIBLE).name("&7Return to profiles").create();
        inv.setItem(0, back);

        return inv;
    }

    @Override
    public void onBottomClick(InventoryClickEvent event){
        ItemStack item = event.getCurrentItem();
        if(item == null)
            return;

        CustomItemManager customItemManager = plugin.getCustomItemManager();
        CustomItem handler = customItemManager.getRegistry().getHandler(item);
        AbilityManager abilityManager = plugin.getAbilityManager();
        if(!(handler instanceof AbilityGemItem))
            return;

        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        String abilityID = Util.getNBTString(plugin, item, "ABILITY_ID");
        String[] playerAbilities = profileData.getAbilityIDs();

        Ability ability = abilityManager.getAbilityByID(abilityID);

        if(abilityManager.hasAbility(profileData, ability)){
            player.sendMessage(Util.formatColour("&7You cannot slot two of the same abilities."));
            return;
        }
        if(!abilityManager.hasEmptyAbilitySlot(profileData)){
            player.sendMessage(Util.formatColour("&7You do not have any empty ability slots."));
            return;
        }

        abilityManager.addAbilityToPlayer(profileData, ability);

        //will always be 1 stack
        player.getInventory().remove(item);

        event.setCancelled(true);

        Inventory inv = new AbilityInventory(plugin, player).getInventory();
        player.openInventory(inv);
    }
}






















