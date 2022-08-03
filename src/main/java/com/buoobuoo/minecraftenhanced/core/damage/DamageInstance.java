package com.buoobuoo.minecraftenhanced.core.damage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class DamageInstance {
    //raw health
    private double damageDealt = 0;
    private Entity ent;
    private ItemStack weapon;
    private String damageIndicatorPrefix = "";

    public DamageInstance(Entity ent, ItemStack weapon){
        this.ent = ent;
        this.weapon = weapon;
    }

    public void reduceDamageDealt(double amt){
        damageDealt -= amt;
    }

    public void increasedDamageDealt(double amt){
        damageDealt += amt;
    }
}
