package com.buoobuoo.minecraftenhanced.core.ability;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Getter
public abstract class Ability {
    private final AbilityType type;
    private final String name;
    private final String id;
    private final String[] lore;
    private final double manaCost;
    private final int abilityCooldown;

    private final double radius = 5;

    public Ability(AbilityType type, double manaCost, int abilityCooldown, String id, String name, String... lore){
        this.type = type;
        this.name = name;
        this.id = id;
        this.lore = lore;
        this.manaCost = manaCost;
        this.abilityCooldown = abilityCooldown;
    }

    public String[] getLore(AbilityCastType type, String[] lore){
        return Util.addToArr(lore, "", "&r&f" + String.format("%.2f", getAbilityCooldown()/20d) + "s &7Cooldown", "&7Consumes &f" + getManaCost() + " &7mana on use");
    }

    public String[] getLore(AbilityCastType type){
        return Util.addToArr(lore, "", "&r&f" + String.format("%.2f", getAbilityCooldown()/20d) + "s &7Cooldown", "&7Consumes &f" + getManaCost() + " &7mana on use");
    }

    public String[] getLore(){
        return Util.addToArr(lore, "", "&r&f" + String.format("%.2f", getAbilityCooldown()/20d) + "s &7Cooldown", "&7Consumes &f" + getManaCost() + " &7mana on use");
    }

    public void cast(MinecraftEnhanced plugin, Player player, Entity target, double effectiveness) {
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        AbilityManager abilityManager = plugin.getAbilityManager();
        double profileMana = profileData.getMana();

        if (abilityManager.getAbilityCoolDown(profileData, this) > 0)
            return;

        if(profileMana <= getManaCost())
            return;

        if(!canCast(profileData, effectiveness))
            return;

        onCast(plugin, player, target, effectiveness);

        profileData.setMana(profileMana - getManaCost());
        abilityManager.setAbilityCoolDown(profileData, this);
    }

    public abstract void onCast(MinecraftEnhanced plugin, Player player, Entity target, double effectiveness);

    public boolean canCast(ProfileData profileData, double effectiveness){
        return true;
    }
}

























