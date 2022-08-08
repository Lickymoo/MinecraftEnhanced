package com.buoobuoo.minecraftenhanced.core.ability.impl;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.ability.Ability;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityCastType;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityType;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.status.StatusEffect;
import com.buoobuoo.minecraftenhanced.core.status.impl.FlagellantStatusEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FlagellantAbility extends Ability {

    private static final double percent = 25;
    private static final double asDamage = 80;

    public FlagellantAbility() {
        super(AbilityType.STRENGTH, 5, 40, "FLAGELLANT", "&fFlagellant", "&r&7Expend &f" + percent + "% &7of your max health", "&r&7in turn gain &f" +asDamage + "% &7of it", "&r&7as damage in your next hit", "", "&r&7Cannot be cast again until next hit", "&r&7Cannot be cast under &f" + percent + "% &7health");
    }

    @Override
    public void onCast(MinecraftEnhanced plugin, Player player, Entity ent, double effectiveness) {
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        double val = (asDamage/100) * effectiveness;

        double health = profileData.getHealth();
        double maxHealth = profileData.getStatInstance().getMaxHealth();
        double removedHealth = (percent/100) * maxHealth;
        health -= removedHealth;
        profileData.setHealth(health);

        profileData.getStatusEffects().add(new FlagellantStatusEffect(plugin, profileData, removedHealth * val));

    }

    @Override
    public String[] getLore(AbilityCastType type){
        double effectiveness = type.getEffectiveness();
        double val = asDamage * effectiveness;
        String[] lore = new String[]{
                "&r&7Expend &f" + percent + "% &7of your max health,", "&r&7in turn gain &f" + val + "% &7of it", "&r&7as damage in your next hit", "", "&r&7Cannot be cast again until next hit", "&r&7Cannot be cast under &f" + percent + "% &7health"
        };

        return super.getLore(type, lore);
    }

    @Override
    public boolean canCast(ProfileData profileData, double effectiveness){
        double health = profileData.getHealth();
        double maxHealth = profileData.getStatInstance().getMaxHealth();
        double removedHealth = (percent/100) * maxHealth;

        if(health < removedHealth)
            return false;

        if(StatusEffect.hasStatusEffect(profileData, FlagellantStatusEffect.class))
            return false;

        return true;
    }
}




























