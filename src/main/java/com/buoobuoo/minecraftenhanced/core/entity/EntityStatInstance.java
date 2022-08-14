package com.buoobuoo.minecraftenhanced.core.entity;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.AttributedItem;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.Pair;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@Getter
@Setter
public class EntityStatInstance {

    private double maxHealth = 0;
    private double currentHealth = 0; // for entities
    private double maxMana = 0;
    private double defense;

    private double manaRegenPS = 1;
    private double healthRegenPS = 1;

    private double walkSpeed = 0;


    private double damageDealt = 0;
    private double critStrikeChance = 0;
    private double critStrikeMulti = 100;


    private final Entity entity;

    public EntityStatInstance(MinecraftEnhanced plugin, ProfileData profileData){
        //DEFAULTS
        maxHealth = ProfileData.BASE_HEALTH;
        maxMana = ProfileData.BASE_MANA;

        manaRegenPS = ProfileData.BASE_MANA_REGEN_PER_SECOND;
        healthRegenPS = ProfileData.BASE_HEALTH_REGEN_PER_SECOND;

        walkSpeed = ProfileData.BASE_WALK_SPEED;

        CustomItemManager itemManager = plugin.getCustomItemManager();

        Player player = Bukkit.getPlayer(profileData.getOwnerID());
        entity = player;
        for(Pair<ItemStack, AttributedItem> pair : Util.getEquippedAttributedItems(plugin, player)){
            AttributedItem item = pair.getRight();
            if(!item.meetsRequirement(profileData))
                continue;

            ItemStack itemStack = pair.getLeft();
            item.onCalc(plugin, itemStack, this);
        }
        currentHealth = profileData.getHealth();

        damageDealt = Math.max(1, damageDealt);
    }


    public EntityStatInstance(MinecraftEnhanced plugin, CustomEntity customEntity){
        //DEFAULTS
        maxHealth = customEntity.maxHealth();

        entity = customEntity.asEntity().getBukkitEntity();

        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        currentHealth = pdc.get(new NamespacedKey(plugin, "HEALTH"), PersistentDataType.DOUBLE);
        damageDealt = pdc.get(new NamespacedKey(plugin, "DAMAGE"), PersistentDataType.DOUBLE);
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

    public void increaseDamageDealt(double amt){
        damageDealt += amt;
    }

    public void increaseCritStrikeChance(double amt){
        critStrikeChance += amt;
    }

    public void increaseCritStrikeMulti(double amt){
        critStrikeMulti += amt;
    }
}
