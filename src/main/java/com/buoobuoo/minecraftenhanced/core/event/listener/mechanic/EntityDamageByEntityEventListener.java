package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import com.buoobuoo.minecraftenhanced.core.damage.DamageType;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityDamageByEntityEventListener implements Listener {

    private final MinecraftEnhanced plugin;

    public EntityDamageByEntityEventListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void listen(EntityDamageByEntityEvent event){
        if(event.isCancelled())
            return;

        double effectiveness = 1;

        if(event.getCause() == EntityDamageEvent.DamageCause.CUSTOM)
            effectiveness = event.getDamage();

        if(event.getDamager() instanceof Player && !(event.getEntity() instanceof Player))
            playerHitEnemy(event, (Player)event.getDamager(), DamageType.MELEE, effectiveness);

        //Bow
        if(event.getDamager() instanceof Arrow &&  !(event.getEntity() instanceof Player)) {
            Arrow arrow = (Arrow) event.getDamager();
            if(arrow.getShooter() instanceof Player) {
                arrow.remove();
                Player player = (Player) arrow.getShooter();
                playerHitEnemy(event, player, DamageType.RANGED, effectiveness);
            }
        }

        if(!(event.getDamager() instanceof Player) && event.getEntity() instanceof Player)
            enemyHitPlayer(event);

        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
            playerHitPlayer(event);

    }

    private void playerHitPlayer(EntityDamageByEntityEvent event){
        Player damager = (Player)event.getDamager();
        Player damagee = (Player)event.getEntity();


    }

    private void enemyHitPlayer(EntityDamageByEntityEvent event){
        event.setDamage(0);
        Player player = (Player)event.getEntity();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        Entity entity = event.getDamager();

        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        String id = pdc.get(new NamespacedKey(plugin, "ENT_ID"), PersistentDataType.STRING);

        if(id == null)
            return;

        double damage = pdc.get(new NamespacedKey(plugin, "DAMAGE"), PersistentDataType.DOUBLE);
        double health = profileData.getHealth();
        profileData.setHealth(health - damage);
    }

    private void playerHitEnemy(EntityDamageByEntityEvent event, Player player, DamageType type, double effectiveness){
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        event.setDamage(0);

        Damageable entity = (Damageable) event.getEntity();
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        String id = pdc.get(new NamespacedKey(plugin, "ENT_ID"), PersistentDataType.STRING);

        if(id == null)
            return;

        DamageInstance damageInstance = plugin.getDamageManager().calculateDamage(player, entity, type, effectiveness);

        double health = pdc.get(new NamespacedKey(plugin, "HEALTH"), PersistentDataType.DOUBLE);
        double damageDealt = damageInstance.getDamageDealt();
        health -= damageDealt;
        pdc.set(new NamespacedKey(plugin, "HEALTH"), PersistentDataType.DOUBLE, health);

        CustomEntity ent = plugin.getEntityManager().getHandlerByEntity(event.getEntity());
        if(ent != null)
            ent.onHit(profileData);

        if(health <= 0)
            entity.setHealth(0);
    }
}

































