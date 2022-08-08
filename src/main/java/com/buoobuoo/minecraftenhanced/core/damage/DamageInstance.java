package com.buoobuoo.minecraftenhanced.core.damage;

import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class DamageInstance {
    //raw health
    private double damageDealt = 1;
    private double critStrikeChance = 0;
    private double critStrikeMulti = 100;

    private Entity ent;
    private ItemStack weapon;
    private String damageIndicatorPrefix = "";
    private ProfileData damager;

    public DamageInstance(ProfileData damager, Entity ent, ItemStack weapon){
        this.ent = ent;
        this.weapon = weapon;
        this.damager = damager;
    }

    public void reduceDamageDealt(double amt){
        damageDealt -= amt;
    }

    public void increaseDamageDealt(double amt){
        damageDealt += amt;
    }

    public void reduceCritStrikeChance(double amt){
        critStrikeChance -= amt;
    }

    public void increaseCritStrikeChance(double amt){
        critStrikeChance += amt;
    }

    public void reduceCritStrikeMulti(double amt){
        critStrikeMulti -= amt;
    }

    public void increaseCritStrikeMulti(double amt){
        critStrikeMulti += amt;
    }
}
