package com.buoobuoo.minecraftenhanced.core.entity.impl.util;

import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

//Used to mirror actions onto npc
public class TagEntity extends ArmorStand implements CustomEntity {
    private final String text;

    public TagEntity(Location loc, String text) {
        super(EntityType.ARMOR_STAND, ((CraftWorld) loc.getWorld()).getHandle());

        this.text = text;

        this.setInvulnerable(true);
        this.setSilent(true);
        this.setMarker(true);
        this.setInvisible(true);
        this.setBoundingBox(new AABB(0, 0, 0, 0, 0, 0));
        this.setCustomName(Component.translatable(text));
        this.setCustomNameVisible(true);
    }

    @Override
    public String entityID() {
        return "TAG_ENTITY";
    }

    @Override
    public String entityName() {
        return "TAG_ENTITY";
    }

}
