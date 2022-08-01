package com.buoobuoo.minecraftenhanced.core.dialogue;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class DialogueManager {
    private final MinecraftEnhanced plugin;

    private final HashMap<UUID, DialogueInstance> instanceMap = new HashMap<>();

    public DialogueManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    public void incrementDialogue(Player player){
        UUID uuid = player.getUniqueId();
        if(!instanceMap.containsKey(uuid))
            return;

        DialogueInstance instance = instanceMap.get(uuid);
        instance.next();
    }

    public void startDialogue(DialogueTrack track, Player player){
        DialogueInstance inst = new DialogueInstance(this, track, player);
        inst.next();
        registerInstance(inst);
    }

    public void registerInstance(DialogueInstance instance){
        instanceMap.put(instance.getPlayerUUID(), instance);
    }

    public void unregisterInstance(DialogueInstance instance){
        instanceMap.remove(instance.getPlayerUUID());
    }
}
