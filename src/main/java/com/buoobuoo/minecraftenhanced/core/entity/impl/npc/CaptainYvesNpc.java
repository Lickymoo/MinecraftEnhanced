package com.buoobuoo.minecraftenhanced.core.entity.impl.npc;

import com.buoobuoo.minecraftenhanced.core.entity.AbstractNpc;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import net.minecraft.world.phys.AABB;
import org.bukkit.Location;

public class CaptainYvesNpc extends AbstractNpc {

    public CaptainYvesNpc(Location loc) {
        super(loc);
        this.setBoundingBox(new AABB(0, 0, 0, 0, 0, 0));
    }

    @Override
    public String entityID() {
        return "NPC_CAPTAIN_YVES";
    }

    @Override
    public String entityName() {
        return "Captain Yves";
    }

    @Override
    public double maxHealth() {
        return 10;
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

    @Override
    public String overrideTag() {
        return CharRepo.SPEECH.getCh();
    }

    @Override
    public String textureSignature() {
        return "";
    }

    @Override
    public String textureBase64() {
        return "";
    }
}
