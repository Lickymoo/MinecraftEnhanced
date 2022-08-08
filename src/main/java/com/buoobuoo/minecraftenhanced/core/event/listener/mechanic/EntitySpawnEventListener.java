package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntitySpawnEventListener implements Listener {

    private final MinecraftEnhanced plugin;

    public EntitySpawnEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void listen(EntitySpawnEvent event){
        Entity ent = event.getEntity();
        if(!(ent instanceof LivingEntity) || ent instanceof ArmorStand)
            return;

        PersistentDataContainer pdc = ent.getPersistentDataContainer();
        String id = pdc.get(new NamespacedKey(plugin, "ENT_ID"), PersistentDataType.STRING);

        if(id != null) {
            return;
        }

        event.setCancelled(true);
    }
}
