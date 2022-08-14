package com.buoobuoo.minecraftenhanced.core.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import org.bukkit.craftbukkit.v1_19_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;

public class PDCSerializer {
    /**
    * Entity's PDC -> String
    */
    public static String serializePdc(PersistentDataContainer obj) {
        CraftPersistentDataContainer pdc = (CraftPersistentDataContainer) obj;
        CompoundTag tag = pdc.toTagCompound();
        return tag.getAsString();
    }

    /**
     * String -> Entity's PDC
     */
    public static void deserializePdc(String serializedPdc, Entity entity) {
        try {
            CompoundTag tag = TagParser.parseTag(serializedPdc);
            CraftPersistentDataContainer pdc = (CraftPersistentDataContainer) entity.getPersistentDataContainer();
            pdc.putAll(tag);
        }catch (CommandSyntaxException ex){
            ex.printStackTrace();
        }
    }


}
