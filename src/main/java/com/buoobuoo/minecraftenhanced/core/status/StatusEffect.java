package com.buoobuoo.minecraftenhanced.core.status;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public abstract class StatusEffect {
    protected final String name;
    protected final String[] description;
    protected int durationTicks;
    protected int remainingTicks;

    protected final ProfileData profileData;
    protected final MinecraftEnhanced plugin;
    protected final String icon;

    public StatusEffect(MinecraftEnhanced plugin, ProfileData profileData, CharRepo icon, String name, String... description){
        this.name = name;
        this.description = description;
        this.profileData = profileData;
        this.plugin = plugin;
        this.icon = icon.getCh();
        onStart();
        onTick();
    }

    public StatusEffect(MinecraftEnhanced plugin, ProfileData profileData, CharRepo icon, int durationTicks, String name, String... description){
        this.name = name;
        this.description = description;
        this.profileData = profileData;
        this.plugin = plugin;
        this.icon = icon.getCh();
        this.durationTicks = durationTicks;
        this.remainingTicks = durationTicks;

        onStart();

        new BukkitRunnable(){

            int i = 0;
            @Override
            public void run() {
                if(i >= durationTicks) {
                    this.cancel();
                    end();
                }
                remainingTicks -= 1;
                onTick();
                i++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public abstract void onStart();

    public abstract void onTick();

    public abstract void onEnd();

    private void end(){
        onEnd();
        profileData.getStatusEffects().remove(this);
    }

    public static boolean hasStatusEffect(ProfileData profileData, Class<? extends StatusEffect> clazz){
        for(StatusEffect effect : profileData.getStatusEffects()){
            if(effect.getClass() == clazz)
                return true;
        }
        return false;
    }

    public static void removeStatusEffect(ProfileData profileData, Class<? extends StatusEffect> clazz){
        StatusEffect e = null;
        for(StatusEffect effect : profileData.getStatusEffects()){
            if(effect.getClass() == clazz)
                e = effect;
        }
        if(e == null)
            return;

        profileData.getStatusEffects().remove(e);
    }
}
