package com.buoobuoo.minecraftenhanced.core.dialogue;

import java.util.function.Consumer;

public class ExecutableSection extends Section{
    private final Consumer<DialogueInstance> consumer;

    public ExecutableSection(Consumer<DialogueInstance> consumer){
        this.consumer = consumer;
    }

    @Override
    public void execute(DialogueInstance instance) {
        consumer.accept(instance);
    }
}
