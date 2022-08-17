package com.buoobuoo.minecraftenhanced.core.util;

import com.buoobuoo.minecraftenhanced.core.entity.impl.util.EmptyEntity;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicFrame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LocationUtil {

    public static CinematicFrame[] lerp(Location from, Location to, int ticks, EmptyEntity toTeleport) {
        double xDiff = to.getX() - from.getX();
        double yDiff = to.getY() - from.getY();
        double zDiff = to.getZ() - from.getZ();
        float yawDiff = to.getYaw() - from.getYaw();
        float pitchDiff = to.getPitch() - from.getPitch();

        Entity entity = toTeleport.getBukkitEntity();
        Location framePos = from.clone();
        List<CinematicFrame> sequences = new ArrayList<>();
        for(int i = 1; i <= ticks; i++) {
            int finalI = i;
            sequences.add(new CinematicFrame(1, e -> {
                double x = from.getX() + ((xDiff/(double)ticks) * finalI);
                double y = from.getY() + ((yDiff/(double)ticks) * finalI);
                double z = from.getZ() + ((zDiff/(double)ticks) * finalI);
                float yaw = from.getYaw() + ((yawDiff/(float)ticks) * finalI);
                float pitch = from.getPitch() + ((pitchDiff/(float)ticks) * finalI);
                entity.teleport(new Location(from.getWorld(), x, y, z, yaw, pitch));

            }));
        }
        return sequences.toArray(new CinematicFrame[sequences.size()]);
    }

    public static CinematicFrame[] lerpBlock(Location from, Location to, int ticks, Material block, Player player) {
        double xDiff = to.getX() - from.getX();
        double yDiff = to.getY() - from.getY();
        double zDiff = to.getZ() - from.getZ();
        float yawDiff = to.getYaw() - from.getYaw();
        float pitchDiff = to.getPitch() - from.getPitch();

        Location framePos = from.clone();
        List<CinematicFrame> sequences = new ArrayList<>();

        for(int i = 1; i <= ticks; i++) {
            sequences.add(
                    new CinematicFrame(1, e -> {
                        Location nextLoc = cloneAdd(framePos, xDiff/ticks, yDiff/ticks, zDiff/ticks, yawDiff/ticks, pitchDiff/ticks);
                        if(nextLoc.getBlock().getType() == Material.AIR)
                            player.sendBlockChange(framePos, Material.AIR.createBlockData());

                        add(framePos, xDiff/ticks, yDiff/ticks, zDiff/ticks, yawDiff/ticks, pitchDiff/ticks);

                        if(framePos.getBlock().getType() != block)
                            player.sendBlockChange(framePos, block.createBlockData());
                    } ));
        }
        return sequences.toArray(new CinematicFrame[sequences.size()]);
    }

    public static String formatString(Location loc) {
        return "("+String.format("%.2f",loc.getX())+","+String.format("%.2f",loc.getY())+","+String.format("%.2f",loc.getZ())+")";
    }

    public static String getDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 22.5D)) {
            return "W";
        }
        if ((22.5D <= rotation) && (rotation < 67.5D)) {
            return "NW";
        }
        if ((67.5D <= rotation) && (rotation < 112.5D)) {
            return "N";
        }
        if ((112.5D <= rotation) && (rotation < 157.5D)) {
            return "NE";
        }
        if ((157.5D <= rotation) && (rotation < 202.5D)) {
            return "E";
        }
        if ((202.5D <= rotation) && (rotation < 247.5D)) {
            return "SE";
        }
        if ((247.5D <= rotation) && (rotation < 292.5D)) {
            return "S";
        }
        if ((292.5D <= rotation) && (rotation < 337.5D)) {
            return "SW";
        }
        if ((337.5D <= rotation) && (rotation < 360.0D)) {
            return "W";
        }
        return "";
    }

    public static Location add(Location loc, double x, double y, double z, float yaw, float pitch) {
        loc.add(x, y, z);
        loc.setYaw(loc.getYaw() + yaw);
        loc.setPitch(loc.getPitch() + pitch);
        return loc;
    }

    public static Location cloneAdd(Location loc, double x, double y, double z, float yaw, float pitch) {
        Location ret = loc.clone();
        ret.add(x, y, z);
        ret.setYaw(ret.getYaw() + yaw);
        ret.setPitch(ret.getPitch() + pitch);
        return ret;
    }


}




