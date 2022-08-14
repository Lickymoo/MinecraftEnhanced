package com.buoobuoo.minecraftenhanced.core.entity.impl.npc;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.AbstractNpc;
import com.buoobuoo.minecraftenhanced.core.player.PlayerManager;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import com.buoobuoo.minecraftenhanced.core.quest.impl.act1.ACT1_MQ1;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import net.minecraft.world.phys.AABB;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class AramoreBlacksmithNpc extends AbstractNpc {

    public AramoreBlacksmithNpc(Location loc) {
        super(loc);
        this.setBoundingBox(new AABB(0, 0, 0, 0, 0, 0));
        this.showIf(player -> {
            MinecraftEnhanced plugin = MinecraftEnhanced.getInstance();
            PlayerManager playerManager = plugin.getPlayerManager();
            QuestManager questManager = plugin.getQuestManager();

            ProfileData profileData = playerManager.getProfile(player);

            return questManager.hasCompletedQuest(ACT1_MQ1.class, profileData);
        });
    }

    @Override
    public String entityID() {
        return "NPC_ARAMORE_BLACKSMITH";
    }

    @Override
    public String entityName() {
        return "Blacksmith";
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
