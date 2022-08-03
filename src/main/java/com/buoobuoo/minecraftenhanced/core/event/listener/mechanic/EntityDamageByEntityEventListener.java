package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityDamageByEntityEventListener implements Listener {

    private final MinecraftEnhanced plugin;

    public EntityDamageByEntityEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void listen(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player))
            return;

        event.setDamage(0);

        Player player = (Player)event.getDamager();
        Damageable entity = (Damageable) event.getEntity();
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        String id = pdc.get(new NamespacedKey(plugin, "ENT_ID"), PersistentDataType.STRING);

        if(id == null)
            return;

        DamageInstance damageInstance = plugin.getDamageManager().calculateDamage(player, entity);

        double health = pdc.get(new NamespacedKey(plugin, "HEALTH"), PersistentDataType.DOUBLE);
        double damageDealt = damageInstance.getDamageDealt();
        health -= damageDealt;
        pdc.set(new NamespacedKey(plugin, "HEALTH"), PersistentDataType.DOUBLE, health);

        if(health <= 0)
            entity.setHealth(0);

        player.sendMessage("health: " + health);
        player.sendMessage("dealt " + damageDealt + " damage");
    }
}

































