package com.buoobuoo.minecraftenhanced.core.entity.impl.npc;

import com.buoobuoo.minecraftenhanced.core.entity.AbstractNpc;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.AdditionalTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.HideHealthTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.HideNameTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.Invulnerable;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import net.minecraft.world.phys.AABB;
import org.bukkit.Location;

public class CaptainYvesNpc extends AbstractNpc implements Invulnerable, HideNameTag, HideHealthTag, AdditionalTag {

    public CaptainYvesNpc(Location loc) {
        super(loc);
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
    public String textureSignature() {
        return "lB7MSGTHRroTxTDRIRBmvMFOTN4jrAMIwPTMXyUVnQF8T1ctzZVlX1OKy6XeWd8n8vWAb2nB8ub9QKFbRYvG/EarP7qeQAS2YIVdVsRpgWYfbpR23U13WpYAIya88sg7rqohSW9TQGBw/cq8jJcwN1aDuxtMneuCwTWdlxwZvTZhXFHYTkV106gSXiKI1fSi61tqeekaGf7+B9i+kj2ktzEjgo+pOb1OeRVqq4MfCCFg+knb77hCtAQt7XtIfHmfihogrgWZaei6oqKchVEuRvu34XI8f8sm/FrBa6I0qoq/66iQBGusXiemTPwwg/vUr534+cE0cLijkREoup3z2NkB0/7i5i/vx2g0TTaCDB/sT7I0NNgXPfj0W/+3veRxNJU+vFpL7B7WdJrA3vEuhDybAKCOheLPhoJu7QgTV8znESp1F9XfVQuwiNeSyW4nyuepQAbqo0SxnVUxioj+frxb8eL2l2M+ritbiof6I9za1IQm15SGD+OJ+buE6a7KiQXmj6hkEfiD6FVgkWGn2gXihozXag7JqKlRxgUOG/KIKmxaLK9bMjbhHY4/f9vfwSxA+hcxLp4p67/KHKxfFcNH3um5zSlXJ9mZps8Nxk5HgaV81gE2y6zktkTlSIFDGpg/TgEBsdAex8mH5o4a9ZBswSfaDUE0ODgILefRYuc=";
    }

    @Override
    public String textureBase64() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY2MDU0MDk0OTI0OSwKICAicHJvZmlsZUlkIiA6ICI0NmY3N2NjNmQ2MjU0NjEzYjc2NmYyZDRmMDM2MzZhNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaXNzV29sZiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80NjBkZjIxNjlmZGVjMGQ1ZGJhZjhmMDIzYTljYzIxMjRkYzY5M2Y2ZDZlNzUxY2E4YTczMzI2MGY0Y2QyZDQ5IgogICAgfQogIH0KfQ==";
    }


    @Override
    public String overrideTag() {
        return "Captain Yves" + " \n " + CharRepo.SPEECH;
    }
}
