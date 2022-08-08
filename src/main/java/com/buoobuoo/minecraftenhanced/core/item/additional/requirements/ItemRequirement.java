package com.buoobuoo.minecraftenhanced.core.item.additional.requirements;

import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;

@Getter
@Setter
public abstract class ItemRequirement implements Listener {
    protected final String name;
    protected final String id;
    protected final double value;

    public ItemRequirement(String name, String id, double value){
        this.name = name;
        this.id = id;
        this.value = value;
    }

    public abstract String itemLore(ProfileData profileData);

    public abstract boolean meetsRequirement(ProfileData profileData);

    protected String getLorePrefix(ProfileData profileData){
        //Cross \u2715
        //Tick \u2714

        String prefix;
        if(profileData == null){
            prefix = "&7&l-";
        }else{
            if(meetsRequirement(profileData)){
                prefix = "&a&l\u2714";
            }else{
                prefix = "&c&l\u2715";
            }
        }
        return prefix;
    }
}
