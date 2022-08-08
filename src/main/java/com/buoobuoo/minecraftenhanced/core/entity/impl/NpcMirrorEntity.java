package com.buoobuoo.minecraftenhanced.core.entity.impl;

import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

//Used to mirror actions onto npc
public class NpcMirrorEntity extends Zombie implements CustomEntity {
    public NpcMirrorEntity(Location loc) {
        super(EntityType.ZOMBIE, ((CraftWorld) loc.getWorld()).getHandle());

        this.setAggressive(false);
        this.setInvulnerable(true);
        this.setSilent(true);
    }

    @Override
    public void entityTick(){
        PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false);
        invisibility.apply((org.bukkit.entity.LivingEntity) asEntity().getBukkitEntity());
    }

    @Override
    public String entityID() {
        return "NPC_MIRROR";
    }

    @Override
    public String entityName() {
        return "";
    }

    @Override
    public String overrideTag() {
        return "";
    }

    @Override
    public double maxHealth() {
        return 0;
    }

    @Override
    public double damage() {
        return 0;
    }

    @Override
    public double tagOffset() {
        return 0;
    }

    @Override
    public int entityLevel() {
        return 0;
    }

    @Override
    public boolean showHealth() {
        return false;
    }
}
