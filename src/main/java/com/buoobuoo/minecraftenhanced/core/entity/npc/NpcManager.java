package com.buoobuoo.minecraftenhanced.core.entity.npc;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class NpcManager implements Listener {
    private final MinecraftEnhanced plugin;
    private List<NpcInstance> instanceList = new ArrayList<>();

    public NpcManager(MinecraftEnhanced plugin) {
        this.plugin = plugin;
    }

    public void registerNpcInstance(NpcInstance npcInstance){
        this.instanceList.add(npcInstance);
    }

    public NpcInstance getInstanceByEntityID(int id){
        for(NpcInstance instance : instanceList){
            if(instance.getEntity().getId() == id)
                return instance;
        }
        return null;
    }

    public NpcInstance getInstanceByInstID(int id){
        for(NpcInstance instance : instanceList){
            if(instance.getId() == id)
                return instance;
        }
        return null;
    }

    public void cleanup(){
        for(NpcInstance instance : instanceList){
            if(!instance.isActive())
                instanceList.remove(instance);
        }
    }

    @EventHandler
    public void onPlayerInteractNpc(PlayerInteractNpcEvent event){
        if(event.getHandler() == null)
            return;
        if(event.getHandler().getNpcHandler() == null)
            return;

        event.getHandler().getNpcHandler().onInteract(event);
    }
}
