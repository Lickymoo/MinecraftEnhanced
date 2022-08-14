package com.buoobuoo.minecraftenhanced.core.player.tempmodifier;

import com.buoobuoo.minecraftenhanced.core.entity.EntityStatInstance;
import lombok.Getter;
import org.bukkit.util.Consumer;

@Getter
public class TemporaryStatModifier {
    private final Consumer<EntityStatInstance> instanceConsumer;
    private final Class<?> modifyingClass;

    public TemporaryStatModifier(Consumer<EntityStatInstance> instanceConsumer){
        this.instanceConsumer = instanceConsumer;
        this.modifyingClass = null;
    }

    public TemporaryStatModifier(Consumer<EntityStatInstance> instanceConsumer, Class<?> clazz){
        this.instanceConsumer = instanceConsumer;
        this.modifyingClass = clazz;
    }
}
