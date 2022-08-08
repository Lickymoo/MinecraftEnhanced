package com.buoobuoo.minecraftenhanced.core.status.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.player.tempmodifier.TemporaryDamageModifier;
import com.buoobuoo.minecraftenhanced.core.status.StatusEffect;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;

public class FlagellantStatusEffect extends StatusEffect {

    private final double damage;

    public FlagellantStatusEffect(MinecraftEnhanced plugin, ProfileData profileData, double damage) {
        super(plugin, profileData, CharRepo.STATUS_EFFECT_FLAGELLANT, "Mark of the Flagellant", "&7Your next attack will inflict &f" + damage +  " &7extra damage");
        this.damage = damage;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onTick() {
        getProfileData().getTemporaryDamageModifiers().add(
                new TemporaryDamageModifier(inst -> {
                    inst.increaseDamageDealt(damage);
                    onEnd();
                })
        );
    }

    @Override
    public void onEnd() {
        StatusEffect.removeStatusEffect(profileData, FlagellantStatusEffect.class);
    }
}
