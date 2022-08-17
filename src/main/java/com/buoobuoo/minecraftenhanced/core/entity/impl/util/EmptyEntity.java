package com.buoobuoo.minecraftenhanced.core.entity.impl.util;

import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.HideHealthTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.HideNameTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.Invisible;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

//Used to mirror actions onto npc
public class EmptyEntity extends ArmorStand implements CustomEntity, HideHealthTag, HideNameTag {

    public EmptyEntity(Location loc) {
        super(EntityType.ARMOR_STAND, ((CraftWorld) loc.getWorld()).getHandle());

        this.setInvulnerable(true);
        this.setSilent(true);
        this.setMarker(true);
        this.setInvisible(true);
        this.setCustomNameVisible(false);
        this.setDestroyOnUnload(true);
    }

    @Override
    public String entityID() {
        return "EMPTY_ENTITY";
    }

    @Override
    public String entityName() {
        return "EMPTY_ENTITY";
    }

}
