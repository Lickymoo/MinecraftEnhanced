package com.buoobuoo.minecraftenhanced.core.player.tempmodifier;

import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import lombok.Getter;
import org.bukkit.util.Consumer;

@Getter
public class TemporaryDamageModifier {
    private final Consumer<DamageInstance> instanceConsumer;

    public TemporaryDamageModifier(Consumer<DamageInstance> instanceConsumer){
        this.instanceConsumer = instanceConsumer;
    }
}
