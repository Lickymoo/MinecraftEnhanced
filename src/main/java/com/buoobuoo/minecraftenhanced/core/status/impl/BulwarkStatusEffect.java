package com.buoobuoo.minecraftenhanced.core.status.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.player.tempmodifier.TemporaryStatModifier;
import com.buoobuoo.minecraftenhanced.core.status.StatusEffect;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;

public class BulwarkStatusEffect extends StatusEffect {

    private final double slowDecrease;
    private final double damageDecrease;

    public BulwarkStatusEffect(MinecraftEnhanced plugin, ProfileData profileData, int durationTicks, double damageDecrease, double slowDecrease) {
        super(plugin, profileData, CharRepo.STATUS_EFFECT_BULWARK, durationTicks, "Bulwark", "&7You take &f" + damageDecrease + "% &fand are slowed by &f" + slowDecrease + "%");
        this.slowDecrease = slowDecrease;
        this.damageDecrease = damageDecrease;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onTick() {
        if(!plugin.getPlayerManager().tempStatModifiersHasClass(profileData, BulwarkStatusEffect.class))
        profileData.getTemporaryStatModifiers().add(new TemporaryStatModifier(
                inst -> {
                    double walkspeed = inst.getWalkSpeed();
                    inst.setWalkSpeed(walkspeed* (slowDecrease/100));
                },
                BulwarkStatusEffect.class
        ));
    }

    @Override
    public void onEnd() {
        StatusEffect.removeStatusEffect(profileData, BulwarkStatusEffect.class);
    }
}
