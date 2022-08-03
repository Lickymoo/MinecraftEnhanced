package com.buoobuoo.minecraftenhanced.core.util;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;

public class Hologram {

    public static ArrayList<Entity> spawnHologram(Location loc, boolean offset, String... lines) {
        int i = 1;
        ArrayList<Entity> stands = new ArrayList<>();
        for(String layer : lines) {

			ArmorStand as;
			if(offset)
				as = loc.getWorld().spawn(loc.clone().add(+0.5D, .5+i*.4D, +0.5D), ArmorStand.class);
			else
				as = loc.getWorld().spawn(loc.clone().add(0, .5+i*.4D, 0), ArmorStand.class);
			as.setGravity(false);
			as.setCustomName(Util.formatColour(layer));
			as.setCustomNameVisible(true);
			as.setInvulnerable(true);
			as.setInvisible(true);
			as.setMarker(true);
			as.setCollidable(false);


            i++;
            stands.add(as);
        }
        return stands;

    }
}