package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityDamageEventListener implements Listener {

    private final MinecraftEnhanced plugin;

    public EntityDamageEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void listen(EntityDamageEvent event){
        EntityDamageEvent.DamageCause cause = event.getCause();
        if(cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            return;
        event.setCancelled(true);

    }
}

































