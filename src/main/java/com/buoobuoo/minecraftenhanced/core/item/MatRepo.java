package com.buoobuoo.minecraftenhanced.core.item;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum MatRepo {

    //GUI ELEMENTS
    INVISIBLE(131001),
    GREEN_TICK(131009),

    ICON_NO_CASTTYPE(131002),
    ICON_CAST_ON_DEATH(131003),
    ICON_CAST_ON_KILL(131004),
    ICON_CAST_ON_MOVE(131005),
    ICON_CAST_ON_CRIT(0),
    ICON_CAST_ON_HIT(0),
    ICON_CAST_ON_DAMAGE_TAKEN(0),

    //CUSTOM ITEMS

    INTELLIGENCE_ORB(133001),
    STRENGTH_ORB(133002),
    DEXTERITY_ORB(133003),

    STARTER_AXE(133004),
    STARTER_SWORD(133005),
    STARTER_BOW(133006),
    STARTER_STAFF(133007);

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
