package com.buoobuoo.minecraftenhanced.core.player;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.Weapon;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProfileStatInstance {

    private double maxHealth = ProfileData.BASE_HEALTH;
    private double maxMana = ProfileData.BASE_MANA;
    private double defense;

    private double manaRegenPS = 1;
    private double healthRegenPS = 1;

    private double walkSpeed = ProfileData.BASE_WALK_SPEED;

    private ProfileData profileData;

    public ProfileStatInstance(MinecraftEnhanced plugin, ProfileData profileData){
        this.profileData = profileData;
        CustomItemManager itemManager = plugin.getCustomItemManager();

        Player player = Bukkit.getPlayer(profileData.getOwnerID());
        List<ItemStack> items = new ArrayList<>();
        ItemStack hand = player.getInventory().getItemInMainHand();

        //check if hand is weapon
        CustomItem handHandler = itemManager.getRegistry().getHandler(hand);
        if(handHandler != null && !(handHandler instanceof Weapon))
            hand = null;


        ItemStack helm = player.getInventory().getHelmet();
        ItemStack chest = player.getInventory().getChestplate();
        ItemStack legs = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        items.add(hand);
        items.add(helm);
        items.add(chest);
        items.add(legs);
        items.add(boots);
        for(ItemStack item : items){
            if(item == null)
                continue;

            CustomItem handler = itemManager.getRegistry().getHandler(item);
            if(handler == null)
                continue;

            if(handler instanceof AttributedItem attributedItem){
                if(!attributedItem.meetsRequirement(profileData))
                    continue;

                attributedItem.onCalc(plugin, item, this);
            }
        }
    }

    public void increaseMaxHealth(double amt){
        this.maxHealth += amt;
    }

    public void increaseMaxMana(double amt){
        this.maxMana += amt;
    }

    public void increaseDefense(double amt){
        this.defense += amt;
    }

    public void increaseManaRegenPS(double amt){
        this.manaRegenPS += amt;
    }

    public void increaseHealthRegenPS(double amt){
        this.healthRegenPS += amt;
    }

    public void increaseWalkSpeed(double amt){
        this.walkSpeed += amt;
    }

}


























