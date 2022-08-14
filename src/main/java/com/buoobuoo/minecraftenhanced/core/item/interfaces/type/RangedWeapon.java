package com.buoobuoo.minecraftenhanced.core.item.interfaces.type;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;

public interface RangedWeapon extends Weapon{

    default Class<? extends Projectile> projectileType(){
        return Arrow.class;
    }
}
