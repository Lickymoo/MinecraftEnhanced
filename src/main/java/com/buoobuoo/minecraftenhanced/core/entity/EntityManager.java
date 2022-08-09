package com.buoobuoo.minecraftenhanced.core.entity;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.impl.RatEntity;
import com.buoobuoo.minecraftenhanced.core.entity.impl.StoneGolemEntity;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.CaptainYvesNpc;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.HelpfulNpc;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.entity.pathfinding.EntityRoutePlanner;
import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateTickEvent;
import com.buoobuoo.minecraftenhanced.core.util.Hologram;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import lombok.Getter;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

@Getter
public class EntityManager implements Listener {

    private final MinecraftEnhanced plugin;
    private final EntityRoutePlanner routePlanner;

    private final ArrayList<Class<? extends CustomEntity>> registeredEntityClasses = new ArrayList<>();
    private final ConcurrentLinkedDeque<CustomEntity> registeredEntities = new ConcurrentLinkedDeque<>();
    private final Map<CustomEntity, List<Entity>> hologramList = new HashMap<>();
    private final Map<PathfinderMob, Location> navigationMap = new HashMap<>();

    public EntityManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
        this.routePlanner = new EntityRoutePlanner(plugin, this);
        plugin.registerEvents(routePlanner);

        setRegisteredEntityClasses(
                CaptainYvesNpc.class,
                HelpfulNpc.class,

                RatEntity.class,
                StoneGolemEntity.class
        );
    }

    public void registerEntities(CustomEntity entity){
        registeredEntities.add(entity);
    }

    public void setRegisteredEntityClasses(Class<? extends CustomEntity>... entities){
        registeredEntityClasses.addAll(List.of(entities));
    }

    public CustomEntity spawnEntity(Class<? extends CustomEntity> entClass, Location location) {
        try {
            CustomEntity entity = entClass.getConstructor(Location.class).newInstance(location);
            entity.spawnEntity(plugin, location);
            return entity;
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }


    public CustomEntity spawnEntity(Class<? extends CustomEntity> entClass, Location location, Class<?>[] constructorClasses, Object... objects) {
        try {
            constructorClasses = Util.addToBeginningOfArray(constructorClasses, Location.class);
            Object[] constructorObjects = Util.addToBeginningOfArray(objects, location);

            CustomEntity entity = entClass.getConstructor(constructorClasses).newInstance(constructorObjects);
            getRegisteredEntities().forEach(e -> System.out.println(e));
            entity.spawnEntity(plugin, location);
            return entity;
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public CustomEntity spawnEntity(Class<? extends CustomEntity> entClass, Location location, Object... objects) {
        try {
            Class<?>[] constructorClasses = new Class<?>[objects.length+1];
            constructorClasses[0] = Location.class;
            for(int i = 0; i < objects.length; i++){
                constructorClasses[i+1] = objects[i].getClass();
            }
            Object[] constructorObjects = Util.addToBeginningOfArray(objects, location);

            CustomEntity entity = entClass.getConstructor(constructorClasses).newInstance(constructorObjects);
            entity.spawnEntity(plugin, location);
            return entity;
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }


    /*
    Util
     */
    public CustomEntity getHandlerByID(int id){
        for(CustomEntity handler : registeredEntities){
            Entity ent = handler.asEntity().getBukkitEntity();
            if(ent.getEntityId() == id)
                return handler;
        }
        return null;
    }

    public CustomEntity getHandlerByName(String name){
        for(CustomEntity handler : registeredEntities){
            if(handler.getClass().getSimpleName().equalsIgnoreCase(name))
                return handler;
        }
        return null;
    }

    public Entity getEntityByID(int id){
        for(CustomEntity handler : registeredEntities){
            Entity ent = handler.asEntity().getBukkitEntity();
            if(ent.getEntityId() == id)
                return ent;
        }
        return null;
    }

    public CustomEntity getHandlerByEntity(Entity entity){
        return getHandlerByID(entity.getEntityId());
    }

    public List<String> getAllHandlerNames(){
        List<String> names = new ArrayList<>();
        for(Class<? extends CustomEntity> entityClass : registeredEntityClasses){
            names.add(entityClass.getSimpleName());
        }
        return names;
    }

    public Class<? extends CustomEntity> getHandlerClassByName(String name){
        for(Class<? extends CustomEntity> clazz : registeredEntityClasses){
            if(clazz.getSimpleName().equalsIgnoreCase(name))
                return clazz;
        }
        return null;
    }

    public double getEntityHealth(CustomEntity entity){
        PersistentDataContainer pdc = entity.asEntity().getBukkitEntity().getPersistentDataContainer();
        return pdc.get(new NamespacedKey(plugin, "HEALTH"), PersistentDataType.DOUBLE);
    }

    public void removeEntity(CustomEntity entity){
        entity.onDestroy();

        for(org.bukkit.entity.Entity hologram : hologramList.get(entity)){
            hologram.remove();
        }
        entity.asEntity().remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
        registeredEntities.remove(entity);
        hologramList.remove(entity);
    }

    public void destroyAll(){
        for(CustomEntity entity : registeredEntities){
           removeEntity(entity);
        }
    }
    /*
    Listeners
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if(getEntityByID(entity.getEntityId()) == null)
            return;

        CustomEntity handler = getHandlerByEntity(entity);

        for(org.bukkit.entity.Entity hologram : hologramList.get(handler)){
            hologram.remove();
        }

        handler.onDeath();

        registeredEntities.remove(handler);
        hologramList.remove(handler);
    }

    @EventHandler
    public void onNpcInteract(PlayerInteractNpcEvent event){
        event.getHandler().onInteract(event);
    }

    @EventHandler
    public void onTick(UpdateTickEvent event){

        for(CustomEntity handler : registeredEntities) {
            handler.entityTick();

            net.minecraft.world.entity.Entity entity = handler.asEntity();
            org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();

            Location loc = bukkitEntity.getLocation();

            double health = getEntityHealth(handler);
            double maxHealth = handler.maxHealth();
            double offset = handler.tagOffset();
            int entityLevel = handler.entityLevel();
            String level = CharRepo.numToTagString(entityLevel);
            String name = handler.entityName();

            int lines = handler.showHealth() ? 2 : 1;

            //do not spawn if override tag is empty
            if("".equals(handler.overrideTag()))
                continue;

            if(!hologramList.containsKey(handler)){
                List<org.bukkit.entity.Entity> holo = Hologram.spawnHologram(plugin, loc, true, new String[lines]);
                hologramList.put(handler, holo);
            }

            List<org.bukkit.entity.Entity> holo = hologramList.get(handler);

            String nameText = handler.overrideTag() == null ? level + " " + name : handler.overrideTag();

            if(handler.showHealth()){
                ArmorStand healthAs = (ArmorStand) holo.get(0);
                healthAs.setCustomName(CharRepo.HEART + Util.formatColour("&f" + (int) health + "/" + (int) maxHealth));
                healthAs.teleport(loc.clone().add(0, 1.8 + offset, 0));

                ArmorStand nameTag = (ArmorStand) holo.get(1);
                nameTag.setCustomName(Util.formatColour(nameText));
                nameTag.teleport(loc.clone().add(0, 2.1 + offset, 0));
            }else{
                ArmorStand nameTag = (ArmorStand) holo.get(0);
                nameTag.setCustomName(Util.formatColour(nameText));
                nameTag.teleport(loc.clone().add(0, 1.8 + offset, 0));
            }
        }
    }

}














































