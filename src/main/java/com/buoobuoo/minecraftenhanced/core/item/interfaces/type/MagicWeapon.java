package com.buoobuoo.minecraftenhanced.core.item.interfaces.type;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public interface MagicWeapon extends Weapon{

    default void spawnParticle(Location loc){
        Particle.DustOptions dustR = new Particle.DustOptions(Color.GREEN, 1F);
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 1, 50, dustR);
    }
}
