package com.buoobuoo.minecraftenhanced.core.entity.persistence;

import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.util.PDCSerializer;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataContainer;

@Getter
public class SuspendedInstance {
    private final CustomEntity entity;
    private final String serializedNBTCompound;
    private final Location location;

    public SuspendedInstance(CustomEntity entity, CompoundTag compoundTag, Location location){
        this.entity = entity;
        this.serializedNBTCompound = compoundTag.toString();
        this.location = location;
    }

}
