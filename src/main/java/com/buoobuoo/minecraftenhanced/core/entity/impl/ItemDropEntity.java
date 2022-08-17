package com.buoobuoo.minecraftenhanced.core.entity.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.HideHealthTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.HideNameTag;
import lombok.Getter;
import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

@Getter
public class ItemDropEntity extends ItemEntity implements CustomEntity, HideNameTag, HideHealthTag {

    private final ItemStack itemStack;

    public ItemDropEntity(Location loc, ItemStack itemStack) {
        super(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(itemStack));
        this.itemStack = itemStack;
    }

    @Override
    public CustomEntity instantiateClone(MinecraftEnhanced plugin){
        EntityManager entityManager = plugin.getEntityManager();

        CustomEntity entity = entityManager.instantiateEntity(this.getClass(), asEntity().getBukkitEntity().getLocation(), new Class<?>[]{ItemStack.class}, itemStack.clone());
        copyTo(entity);
        return entity;
    }

    @Override
    public String entityID() {
        return "DROPPED_ITEM";
    }

    @Override
    public String entityName() {
        return "DroppedItem";
    }

}

