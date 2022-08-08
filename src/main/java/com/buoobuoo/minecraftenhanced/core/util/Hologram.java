package com.buoobuoo.minecraftenhanced.core.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class Hologram {

	public static ArrayList<Entity> spawnHologramRiding(Plugin plugin, Player player, String line){
		Location loc = player.getLocation();
		Slime slime = loc.getWorld().spawn(loc, Slime.class);
		slime.setSize(3);
		slime.setSilent(true);
		slime.setInvisible(true);
		slime.setAI(false);

		ArmorStand as = loc.getWorld().spawn(loc, ArmorStand.class);
		as.setInvisible(true);
		as.setGravity(false);
		as.setCustomName(Util.formatColour(line));
		as.setCustomNameVisible(true);
		as.setInvulnerable(true);
		as.setMarker(true);
		as.setCollidable(false);

		Entity e1 = slime;
		Entity e2 = as;

		player.addPassenger(slime);
		player.addPassenger(as);

		ArrayList<Entity> list = new ArrayList<>();
		list.add(e1);
		list.add(e2);
		return (list);
	}

	public static ArrayList<Entity> spawnHologram(Plugin plugin, Location loc, boolean offset, int killAfterTicks, String... lines) {
		ArrayList<Entity> ents = spawnHologram(plugin, loc, offset, lines);

		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			for(Entity ent : ents){
				ent.remove();
			}
		}, killAfterTicks);
		return ents;
	}

    public static ArrayList<Entity> spawnHologram(Plugin plugin, Location loc, boolean offset, String... lines) {
        int i = 1;
        ArrayList<Entity> stands = new ArrayList<>();
        for(String layer : lines) {
        	layer = layer == null ? "" : layer;

			ArmorStand as;
			as = loc.getWorld().spawn(loc.clone().add(0, 10000, 0), ArmorStand.class);
			if(offset){
				as.teleport(loc.clone().add(+0.5D, .5+i*.4D, +0.5D));
			}else{
				as.teleport(loc.clone().add(0, .5+i*.4D, 0));
			}

			as.setInvisible(true);
			as.setGravity(false);
			as.setCustomName(Util.formatColour(layer));
			as.setCustomNameVisible(true);
			as.setInvulnerable(true);
			as.setMarker(true);
			as.setCollidable(false);

            i++;
            stands.add(as);
        }
        return stands;

    }
}