package com.buoobuoo.minecraftenhanced.core.ability.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.ability.Ability;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityCastType;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityType;
import com.buoobuoo.minecraftenhanced.core.damage.DamageManager;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.vfx.particle.ParticleDirectory;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

public class SlashAbility extends Ability {

    private static final double percentDamage = 80;
    private static final double blocksDistance = 3;

    public SlashAbility() {
        super(AbilityType.STRENGTH, 10, 40, "SLASH", "&fSlash", "&7Slashes enemies within 3 blocks in front of", "&r&7you dealing &f" + percentDamage + "% &7of weapon damage");
    }

    @Override
    public String[] getLore(AbilityCastType type){
        double effectiveness = type.getEffectiveness();
        double val = percentDamage * effectiveness;

        String[] lore = new String[]{
                "&7Slashes enemies within 3 blocks in front of", "&r&7you dealing &f" + val + "% &7of weapon damage"
        };

        return super.getLore(type, lore);
    }

    @Override
    public void onCast(MinecraftEnhanced plugin, Player player, Entity ent, double effectiveness) {

        double cos90 = -Math.cos(Math.toRadians(player.getEyeLocation().getYaw() - 90));
        double sin90 = -Math.sin(Math.toRadians(player.getEyeLocation().getYaw() - 90));

        Location firstLoc = player.getLocation().add(-Math.cos(Math.toRadians(player.getLocation().getYaw())) * 3, 1, -Math.sin(Math.toRadians(player.getLocation().getYaw())) * 3);
        firstLoc.add(cos90, 0, sin90);

        Location secondLoc = player.getLocation().add(Math.cos(Math.toRadians(player.getLocation().getYaw())) * 3, 1, Math.sin(Math.toRadians(player.getLocation().getYaw())) * 3);
        secondLoc.add(cos90, 0, sin90);

        Location thirdLoc = firstLoc.clone().add(cos90 * 6, 0, sin90 * 6);
        Location fourthLoc = secondLoc.clone().add(cos90 * 6, 0, sin90 * 6);

        firstLoc.add(0, 3, 0);
        secondLoc.add(0, 3, 0);
        thirdLoc.add(0, -3, 0);
        fourthLoc.add(0, -3, 0);

        BoundingBox boundingBox = BoundingBox.of(firstLoc, fourthLoc);

        ParticleDirectory.SLASH_INFRONT.playEffect(plugin, player.getLocation(), 1, 2, 3);

        for (Entity e : player.getNearbyEntities(blocksDistance, blocksDistance, blocksDistance)) {

            Location loc = e.getLocation();
            if(boundingBox.contains(loc.toVector())){
                DamageManager damageManager = plugin.getDamageManager();
                CustomEntity handler = plugin.getEntityManager().getHandlerByEntity(e);
                damageManager.handleDamageP2E(player, handler, ((percentDamage/100) * effectiveness));
            }
        }
    }
}
