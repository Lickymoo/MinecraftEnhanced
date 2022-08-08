package com.buoobuoo.minecraftenhanced.core.vfx.particle;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public enum ParticleDirectory {

    LEVELUP{
        @Override
        public void playEffect(MinecraftEnhanced plugin, Location loc, double radius, double maxHeight, float duration) {

            new BukkitRunnable() {
                double t = 0;
                double r = radius;
                public void run() {
                    t = t + Math.PI / 32;
                    double x = r * Math.cos(duration * (t-5)) * ((t-5) * .2);
                    double y = 0.5*t;
                    double z = r * Math.sin(duration * (t-5)) * ((t-5) * .2);

                    if(y >= maxHeight)
                        this.cancel();

                    Location pLoc = loc.clone().add(x, y, z);

                    pLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, pLoc, 1);
                }
            }.runTaskTimer(plugin, 0, 1);

        }
    },
    SLASH_INFRONT{
        @Override
        public void playEffect(MinecraftEnhanced plugin, Location loc, double radius, double maxHeight, float duration) {

            new BukkitRunnable() {
                double t = 0;
                double r = radius;
                public void run() {
                    t = t + Math.PI / 32;
                    double x = r * Math.cos(duration * t);
                    double y = 1;
                    double z = r * Math.sin(duration * t);

                    if(t >= duration)
                        this.cancel();

                    Location pLoc = loc.clone().add(x, y, z);

                    pLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, pLoc, 1);
                }
            }.runTaskTimer(plugin, 0, 1);

        }
    };

    public void playEffect(MinecraftEnhanced plugin, Location loc, double radius, double maxHeight, float duration) {};
}