package com.buoobuoo.minecraftenhanced.core.entity.impl.npc;

import com.buoobuoo.minecraftenhanced.core.entity.AbstractNpc;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.AdditionalTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.HideHealthTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.HideNameTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.Invulnerable;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import net.minecraft.world.phys.AABB;
import org.bukkit.Location;

public class HelpfulNpc extends AbstractNpc implements Invulnerable, HideNameTag, HideHealthTag, AdditionalTag {

    public HelpfulNpc(Location loc) {
        super(loc);
    }

    @Override
    public String entityID() {
        return "NPC_HELPFUL_NPC";
    }

    @Override
    public String entityName() {
        return "???";
    }

    @Override
    public String textureSignature() {
        return "";
    }

    @Override
    public String textureBase64() {
        return "";
    }


    @Override
    public String overrideTag() {
        return CharRepo.SPEECH + "";
    }
}
