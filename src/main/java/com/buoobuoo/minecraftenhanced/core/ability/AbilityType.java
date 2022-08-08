package com.buoobuoo.minecraftenhanced.core.ability;

import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import lombok.Getter;

@Getter
public enum AbilityType {
    STRENGTH(MatRepo.STRENGTH_ORB),
    INTELLIGENCE(MatRepo.INTELLIGENCE_ORB),
    DEXTERITY(MatRepo.DEXTERITY_ORB);

    private MatRepo mat;

    AbilityType(MatRepo mat){
        this.mat = mat;
    }
}
