package com.buoobuoo.minecraftenhanced.core.player.tempmodifier;

import com.buoobuoo.minecraftenhanced.core.player.ProfileStatInstance;
import lombok.Getter;
import org.bukkit.util.Consumer;

@Getter
public class TemporaryStatModifier {
    private final Consumer<ProfileStatInstance> instanceConsumer;
    private final Class<?> modifyingClass;

    public TemporaryStatModifier(Consumer<ProfileStatInstance> instanceConsumer){
        this.instanceConsumer = instanceConsumer;
        this.modifyingClass = null;
    }

    public TemporaryStatModifier(Consumer<ProfileStatInstance> instanceConsumer, Class<?> clazz){
        this.instanceConsumer = instanceConsumer;
        this.modifyingClass = clazz;
    }
}
