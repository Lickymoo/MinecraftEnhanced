package com.buoobuoo.minecraftenhanced.persistence.serialization.impl;

import com.buoobuoo.minecraftenhanced.persistence.serialization.VariableSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer extends VariableSerializer<Location>{

    @Override
    public String serialize(Location obj) {
        if (obj == null) {
            return "";
        }
        return obj.getWorld().getName() + ":" + obj.getX() + ":" + obj.getY() + ":" + obj.getZ() + ":" + obj.getYaw() + ":" + obj.getPitch();
    }

    @Override
    public Location deserialize(String str) {
        if (str == null || str.trim() == "") {
            return null;
        }
        final String[] parts = str.split(":");
        if (parts.length == 4) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            final double x = Double.parseDouble(parts[1]);
            final double y = Double.parseDouble(parts[2]);
            final double z = Double.parseDouble(parts[3]);
            final float pitch = Float.parseFloat(parts[4]);
            final float yaw = Float.parseFloat(parts[5]);
            return new Location(w, x, y, z, yaw, pitch);
        }
        return null;
    }


}