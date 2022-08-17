package com.buoobuoo.minecraftenhanced.core.entity;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.impl.RatEntity;
import com.buoobuoo.minecraftenhanced.core.entity.impl.StoneGolemEntity;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.AramoreBlacksmithNpc;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.CaptainYvesNpc;
import com.buoobuoo.minecraftenhanced.core.entity.impl.npc.HelpfulNpc;
import com.buoobuoo.minecraftenhanced.core.entity.impl.util.TagEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.AdditionalTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.HideHealthTag;
import com.buoobuoo.minecraftenhanced.core.entity.interf.tags.HideNameTag;
import com.buoobuoo.minecraftenhanced.core.entity.pathfinding.EntityRoutePlanner;
import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateTickEvent;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import lombok.Getter;
import net.minecraft.util.datafix.fixes.GoatHornIdFix;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

@Getter
public class EntityManager implements Listener {

    private final MinecraftEnhanced plugin;
    private final EntityRoutePlanner routePlanner;
    private final ChunkWatcher chunkWatcher;

    private final ConcurrentLinkedDeque<Class<? extends CustomEntity>> registeredEntityClasses = new ConcurrentLinkedDeque<>();
    private final ConcurrentLinkedQueue<CustomEntity> registeredEntities = new ConcurrentLinkedQueue<>();
    private final Map<PathfinderMob, Location> navigationMap = new HashMap<>();

    public EntityManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
        this.routePlanner = new EntityRoutePlanner(plugin, this);
        this.chunkWatcher = new ChunkWatcher(plugin, this);
        plugin.registerEvents(routePlanner, chunkWatcher);

        setRegisteredEntityClasses(
                CaptainYvesNpc.class,
                HelpfulNpc.class,

                AramoreBlacksmithNpc.class,

                RatEntity.class,
                StoneGolemEntity.class
        );
    }

    public void initFixtures(){
        //register pemanent fixuture entities
        World mainWorld = MinecraftEnhanced.getMainWorld();
        AramoreBlacksmithNpc aramoreBlacksmithNpc = (AramoreBlacksmithNpc) spawnEntity(AramoreBlacksmithNpc.class, new Location(mainWorld, 162.5, 66, 78.5, 180, 0));
    }

    public void registerEntities(CustomEntity entity){
        registeredEntities.add(entity);
    }

    public void setRegisteredEntityClasses(Class<? extends CustomEntity>... entities){
        registeredEntityClasses.addAll(List.of(entities));
    }

    public CustomEntity instantiateEntity(Class<? extends CustomEntity> entClass, Location location){
        try {
            CustomEntity entity = entClass.getConstructor(Location.class).newInstance(location);
            return entity;
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public CustomEntity instantiateEntity(Class<? extends CustomEntity> entClass, Location location, Class<?>[] constructorClasses, Object... objects) {
        try {
            constructorClasses = Util.addToBeginningOfArray(constructorClasses, Location.class);
            Object[] constructorObjects = Util.addToBeginningOfArray(objects, location);

            CustomEntity entity = entClass.getConstructor(constructorClasses).newInstance(constructorObjects);
            return entity;
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }


    public void spawnInstance(CustomEntity entity, Location location) {
        //load chunk
        //TODO might change to use ChunkWatcher
        Chunk chunk = location.getChunk();
        entity.spawnEntity(plugin, location);
    }

    public CustomEntity spawnEntity(Class<? extends CustomEntity> entClass, Location location) {
        try {
            CustomEntity entity = entClass.getConstructor(Location.class).newInstance(location);
            spawnInstance(entity, location);
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
            spawnInstance(entity, location);
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
            spawnInstance(entity, location);
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
        entity.destroyEntity();
        entity.cleanup();
        registeredEntities.remove(entity);
    }

    //Destroys entity and children WITHOUT unregistering (For suspension)
    public void suspendEntity(CustomEntity entity){
        entity.destroyEntity();
        entity.setSuspended(true);
    }

    public void destroyAll(){
        for(CustomEntity entity : registeredEntities){
           removeEntity(entity);
        }
    }

    public void cleanUp(ProfileData profileData){
        UUID uuid = profileData.getOwnerID();
        for(CustomEntity ent : registeredEntities){
            ent.hiddenPlayers.getOrDefault(ent, new HashSet<>()).remove(uuid);
            ent.shownPlayers.getOrDefault(ent, new ConcurrentSkipListSet<>()).remove(uuid);

            if(ent.hiddenPlayers.getOrDefault(ent, new HashSet<>()).size() == 0 && ent.getInvertHide()) {
                removeEntity(ent);
            }
        }
    }

    public CustomEntity getSuperParent(CustomEntity entity){
        if(entity.getParent() == null)
            return entity;

        CustomEntity parent = entity.getParent();
        return getSuperParent(parent);
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
        event.setDroppedExp(0);

        handler.onDeath();
        removeEntity(handler);
    }

    @EventHandler
    public void onNpcInteract(PlayerInteractNpcEvent event){
        event.getHandler().onInteract(event);
    }

    @EventHandler
    public void onTick(UpdateTickEvent event){

        for(CustomEntity handler : registeredEntities) {
            handler.entityTick();
            updateArmorStands(handler);

        }
    }

    @EventHandler
    public void onCustomItemPickup(PlayerPickupItemEvent event){

        CustomEntity handler = getHandlerByEntity(event.getItem());
        if(handler == null)
            return;

        Player player = event.getPlayer();
        if(!handler.getInvertHide())
            return;

        UUID playerUUID = player.getUniqueId();
        for(UUID uuid : handler.hiddenPlayers()){
            if(uuid.equals(playerUUID))
                return;
        }
        event.setCancelled(true);
    }

    public void updateArmorStands(CustomEntity handler){
        if(handler instanceof TagEntity) return;

        net.minecraft.world.entity.Entity entity = handler.asEntity();
        org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();

        Location loc = bukkitEntity.getLocation();

        double health = getEntityHealth(handler);
        double maxHealth = handler.maxHealth();
        double offset = handler.tagOffset();
        int entityLevel = handler.entityLevel();
        String level = CharRepo.numToTagString(entityLevel);
        String name = handler.entityName();

        //health

        /*
        Tag order
        ADDITIONAL
        NAME
        HEALTH
         */

        Deque<TagEntity> tagEntityDeque = new ArrayDeque<>();

        if(!(handler instanceof HideHealthTag)) {
            TagEntity healthTag = handler.getChild("HEALTH_TAG");

            if (healthTag == null) {
                healthTag = spawnHologram(loc, "");
                handler.addChild("HEALTH_TAG", healthTag);
            }
            tagEntityDeque.add(healthTag);

            String text = CharRepo.HEART + Util.formatColour("&f" + (int) health + "/" + (int) maxHealth);
            ArmorStand armorStand = (ArmorStand) healthTag.asEntity().getBukkitEntity();
            armorStand.setCustomName(Util.formatColour(text));
        }
        if(!(handler instanceof HideNameTag)){
            TagEntity nameTag = handler.getChild("NAME_TAG");

            if (nameTag == null) {
                nameTag = spawnHologram(loc, "");
                handler.addChild("NAME_TAG", nameTag);
            }
            tagEntityDeque.add(nameTag);

            String text = level + " " + name;
            ArmorStand armorStand = (ArmorStand) nameTag.asEntity().getBukkitEntity();
            armorStand.setCustomName(Util.formatColour(text));
        }
        if(handler instanceof AdditionalTag additionalTag){
            String overrideTag = additionalTag.overrideTag();
            int index = 0;

            for(String line : overrideTag.split("[\r\n]+")){
                line = line.trim();
                index++;
                TagEntity nameTag = handler.getChild("ADDITONAL_TAG_" + index);

                if (nameTag == null) {
                    nameTag = spawnHologram(loc, "");
                    handler.addChild("ADDITONAL_TAG_" + index, nameTag);
                }
                tagEntityDeque.add(nameTag);

                ArmorStand armorStand = (ArmorStand) nameTag.asEntity().getBukkitEntity();
                armorStand.setCustomName(Util.formatColour(line));
            }
        }

        final double startY = 1.8 + offset;
        final double step = .3;
        int index = 0;
        Iterator<TagEntity> iterator = tagEntityDeque.iterator();

        while(iterator.hasNext()){
            TagEntity tagEntity = iterator.next();

            ArmorStand armorStand = (ArmorStand) tagEntity.asEntity().getBukkitEntity();
            armorStand.teleport(loc.clone().add(0, startY + (index * step), 0));
            index++;
        }
    }

    public List<TagEntity> spawnHologram(Location loc, int killAfterTicks, String... lines) {
        List<TagEntity> ents = spawnHologram(loc, lines);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for(TagEntity ent : ents){
                removeEntity(ent);
            }
        }, killAfterTicks);
        return ents;
    }

    public List<TagEntity> spawnHologram(Location loc, String... lines){
        int i = 1;
        ArrayList<TagEntity> stands = new ArrayList<>();
        for(String layer : lines) {
            layer = layer == null ? "" : layer;

            TagEntity tag = (TagEntity) spawnEntity(TagEntity.class, loc, layer);
            ArmorStand as = (ArmorStand) tag.asEntity().getBukkitEntity();
            as.teleport(loc.clone().add(0, .5+i*.4D, 0));

            as.setInvisible(true);
            as.setGravity(false);
            as.setCustomName(Util.formatColour(layer));
            as.setCustomNameVisible(true);
            as.setInvulnerable(true);
            as.setMarker(true);
            as.setCollidable(false);

            i++;
            stands.add(tag);
        }
        return stands;
    }

    public TagEntity spawnHologram(Location loc, String line){
        return spawnHologram(loc, new String[]{line}).get(0);
    }
}














































