package com.buoobuoo.minecraftenhanced.core.item;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum MatRepo {

    INVISIBLE(1000),
    ICE_SWORD(1004),

    INTELLIGENCE_ORB(1008),
    STRENGTH_ORB(1009),
    DEXTERITY_ORB(1010),

    ICON_NO_CASTTYPE(2003),
    ICON_CAST_ON_DEATH(2001),
    ICON_CAST_ON_KILL(2002),
    ICON_CAST_ON_MOVE(2004),
    ICON_CAST_ON_CRIT(2005),
    ICON_CAST_ON_HIT(2006),
    ICON_CAST_ON_DAMAGE_TAKEN(2007);

    private final Material mat;
    private final int customModelData;

    MatRepo(Material mat, int customModelData){
        this.mat = mat;
        this.customModelData = customModelData;
    }

    MatRepo(int customModelData){
        this.mat = Material.PAPER;
        this.customModelData = customModelData;
    }
}
