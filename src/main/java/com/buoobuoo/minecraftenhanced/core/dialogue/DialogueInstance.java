package com.buoobuoo.minecraftenhanced.core.dialogue;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class DialogueInstance {

    private final DialogueManager dialogueManager;
    private int index = 0;
    private UUID playerUUID;
    private DialogueTrack track;

    public DialogueInstance(DialogueManager dialogueManager, DialogueTrack track, Player player){
        this.dialogueManager = dialogueManager;
        this.track = track;
        this.playerUUID = player.getUniqueId();
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(playerUUID);
    }

    public void next(){
        if(track.getSize() == index)
            return;

        Section sect = track.getSection(index++);

        if(track.isLast(sect)){
            dialogueManager.unregisterInstance(this);
        }
        sect.execute(this);
    }
}
