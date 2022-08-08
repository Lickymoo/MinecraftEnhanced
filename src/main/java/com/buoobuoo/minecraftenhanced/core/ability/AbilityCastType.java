package com.buoobuoo.minecraftenhanced.core.ability;

import com.buoobuoo.minecraftenhanced.core.item.MatRepo;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum AbilityCastType {
    NONE(MatRepo.ICON_NO_CASTTYPE, "&7None"),
    RLR("&r&fCast on Combo", "&r&7Cast on &fR L R &7combo"),
    RRR("&r&fCast on Combo", "&r&7Cast on &fR R R &7combo"),
    RLL("&r&fCast on Combo", "&r&7Cast on &fR L L &7combo"),
    RRL("&r&fCast on Combo", "&r&7Cast on &fR R L &7combo"),
    CAST_ON_CRIT(MatRepo.ICON_CAST_ON_CRIT, "&r&fCast on Critical Strike", .6, "&r&7Cast when you hit a critical strike", "&r&7Linked Ability is &f40% &7less effective"),
    CAST_ON_HIT(MatRepo.ICON_CAST_ON_HIT, "&r&fCast on Hit", .6, "&r&7Cast when you hit an enemy", "&r&7Linked Ability is &f40% &7less effective"),
    CAST_ON_MOVE(MatRepo.ICON_CAST_ON_MOVE, "&r&fCast on Move", .4, "&r&7Cast when you move", "&r&7Linked Ability is &f60% &7less effective"),
    CAST_ON_KILL(MatRepo.ICON_CAST_ON_KILL, "&r&fCast on Kill", 1.6, "&r&7Cast when you kill an enemy", "&r&7Linked Ability is &f60% &7more effective"),
    CAST_ON_DEATH(MatRepo.ICON_CAST_ON_DEATH, "&r&fCast on Player Death", 2, "&r&7Cast when you die", "&r&7Linked Ability is &f100% &7more effective"),
    CAST_ON_DAMAGE_TAKEN(MatRepo.ICON_CAST_ON_DAMAGE_TAKEN, "&r&fCast on Damage Taken", .4, "&r&7Cast when you take damage", "&r&7Linked Ability is &f70% &7less effective");

    private final String displayName;
    private final String[] displayLore;

    private double effectiveness = 1;
    private Material mat = Material.PAPER;
    private int customModelData = 0;

    AbilityCastType(String displayName, String... displayLore){
        this.displayLore = displayLore;
        this.displayName = displayName;
    }

    AbilityCastType(String displayName, double effectiveness, String... displayLore){
        this.displayLore = displayLore;
        this.displayName = displayName;
        this.effectiveness = effectiveness;
    }

    AbilityCastType(MatRepo mat, String displayName, String... displayLore){
        this.displayLore = displayLore;
        this.displayName = displayName;
        this.mat = mat.getMat();
        this.customModelData = mat.getCustomModelData();
    }

    AbilityCastType(MatRepo mat, String displayName, double effectiveness, String... displayLore){
        this.displayLore = displayLore;
        this.displayName = displayName;
        this.mat = mat.getMat();
        this.customModelData = mat.getCustomModelData();
        this.effectiveness = effectiveness;
    }
}
