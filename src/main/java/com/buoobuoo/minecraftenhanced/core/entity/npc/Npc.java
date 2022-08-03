package com.buoobuoo.minecraftenhanced.core.entity.npc;

import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@Builder
@AllArgsConstructor
public class Npc {
    private String displayName;
    private String skinSignature;
    private String skinTexture;

    public void onInteract(PlayerInteractNpcEvent event){

    }

    public void onSpawn(NpcEntity entity, Player[] players){

    }

    public void onDestroy(NpcEntity entity, Player[] players){

    }
}
