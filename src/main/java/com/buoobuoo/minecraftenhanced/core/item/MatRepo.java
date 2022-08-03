package com.buoobuoo.minecraftenhanced.core.item;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum MatRepo {

    INVISIBLE(1000),
    ICE_SWORD(1004);

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
