package com.buoobuoo.minecraftenhanced.core.entity.interf;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.Pair;
import com.buoobuoo.minecraftenhanced.core.util.ToBooleanFunction;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public interface CustomEntity {

    //lazy way of having instance variables
    public Map<CustomEntity, Boolean> invertHide = new HashMap<>();
    public Map<CustomEntity, Set<UUID>> hiddenPlayers = new HashMap<>();
    public Map<CustomEntity, CustomEntity> parent = new HashMap<>();
    public Map<CustomEntity, Map<String, CustomEntity>> children = new HashMap<>();
    public Map<CustomEntity, ToBooleanFunction<Player>> showIfFunction = new HashMap<>();
    public Map<CustomEntity, Boolean> suspendedMap = new HashMap<>();
    public Map<CustomEntity, Set<UUID>> damagers = new HashMap<>();
    public Map<CustomEntity, Pair<Location, Integer>> originPoint = new HashMap<>();

    public Map<CustomEntity, Boolean> isDestroyed = new HashMap<>();
    public Map<CustomEntity, Boolean> isDead = new HashMap<>();
    public Map<CustomEntity, Boolean> destroyOnUnload = new HashMap<>();
    public Map<CustomEntity, Boolean> doNotDestroy = new HashMap<>();
    public Map<CustomEntity, Boolean> isReady = new HashMap<>();

    String entityID();
    String entityName();
    String overrideTag();
    double maxHealth();
    double damage();
    double tagOffset();
    int entityLevel();
    boolean showHealth();

    default CustomEntity instantiateClone(MinecraftEnhanced plugin){
        EntityManager entityManager = plugin.getEntityManager();

        CustomEntity entity = entityManager.instantiateEntity(this.getClass(), asEntity().getBukkitEntity().getLocation());
        return entity;
    }

    default void copyTo(CustomEntity entity){
        if(invertHide.containsKey(this))
            invertHide.put(entity, invertHide.get(this));

        if(hiddenPlayers.containsKey(this))
            hiddenPlayers.put(entity, hiddenPlayers.get(this));

        if(showIfFunction.containsKey(this))
            showIfFunction.put(entity, showIfFunction.get(this));

        if(damagers.containsKey(this))
            damagers.put(entity, damagers.get(this));
    }

    //abstract
    default void onHit(org.bukkit.entity.Entity entity){}

    default void onDeath(){
        if(isDead())
            return;

        for(UUID uuid : getDamagers()){
            Player player = Bukkit.getPlayer(uuid);
            ProfileData profileData = MinecraftEnhanced.getInstance().getPlayerManager().getProfile(player);
            int exp = profileData.getExperience();
            int playerLevel = profileData.getLevel();
            profileData.setExperience(exp + (int)Math.ceil(calculateExpDrop(playerLevel)));
        }

        setDead(true);
    }

    default void onSpawn(){}

    //inbuilt
    default void destroyEntity(){
        isDestroyed.put(this, true);
        asEntity().remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
        getChildren().forEach(CustomEntity::destroyEntity);
    }

    default void cleanup(){
        invertHide.remove(this);
        hiddenPlayers.remove(this);
        parent.remove(this);
        children.remove(this);
        showIfFunction.remove(this);
        suspendedMap.remove(this);
        damagers.remove(this);
        isDead.remove(this);
        destroyOnUnload.remove(this);
    }

    default double calculateExpDrop(int n){
        int level = entityLevel();
        double g = 25 * level * (1 + level);
        double f = (g/(25*level))*level-Math.abs(level-n)*level;
        return f;
    }

    default void spawnEntity(MinecraftEnhanced plugin, Location loc){
        setDead(false);
        setIsReady(true);
        Entity ent = asEntity();

        ent.setPos(loc.getX(), loc.getY(), loc.getZ());

        PersistentDataContainer pdc = ent.getBukkitEntity().getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, "ENT_ID"), PersistentDataType.STRING, entityID());
        pdc.set(new NamespacedKey(plugin, "HEALTH"), PersistentDataType.DOUBLE, maxHealth());
        pdc.set(new NamespacedKey(plugin, "DAMAGE"), PersistentDataType.DOUBLE, damage());

        ent.getCommandSenderWorld().addFreshEntity(ent);

        plugin.getEntityManager().registerEntities(this);

        onSpawn();
    }

    default Entity asEntity(){
        return (Entity)this;
    }

    default void lookAt(Location loc){
        Location lookDir = loc.clone();
        lookDir.setDirection(loc.subtract(lookDir).toVector());
        moveTo(lookDir);
    }

    default void moveTo(Location loc){
        PathfinderMob pathfinderMob = getPathfinderMob();
        if(pathfinderMob == null){
            asEntity().getBukkitEntity().teleport(loc);
            return;
        }
        pathfinderMob.moveTo(loc.getX(), loc.getY(), loc.getZ());
    }

    default PathfinderMob getPathfinderMob(){
        if(!(asEntity() instanceof PathfinderMob))
            return null;

        return (PathfinderMob) asEntity();
    }

    default void entityTick(){
        if(!isReady())
            return;

        hideTick();
        getChildren().forEach(CustomEntity::entityTick);
    }

    default void hideTick() {
        if(isSuspended() || isDead())
            return;

        hiddenPlayers.putIfAbsent(this, new HashSet<>());
        Set<UUID> hidden = hiddenPlayers.get(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            //if hiddenPlayers = players to show to
            if (getInvertHide() && hidden.contains(uuid)) continue;
            //if hiddenPlayers = players to hide from
            if (!getInvertHide() && !hidden.contains(uuid)) continue;

            if (showIfFunction.get(this) != null){
                if (!showIfFunction.get(this).applyAsBoolean(player)){
                    continue;
                }
            }
            hideEntity(player);
        }
    }

    default void hideEntity(Player player){
        Util.sendPacket(new ClientboundRemoveEntitiesPacket((this).asEntity().getBukkitEntity().getEntityId()), player);
    }

    default void hideToPlayer(Player player){
        hideToPlayer(player.getUniqueId());
    }

    default void hideToPlayer(Player... players){
        for(Player player : players){
            hideToPlayer(player);
        }
    }

    default void hideToPlayer(Collection<UUID> uuids){
        for(UUID uuid : uuids){
            hideToPlayer(uuid);
        }
    }

    default void hideToPlayer(UUID... uuids){
        for(UUID uuid : uuids){
            hideToPlayer(uuid);
        }
    }

    default void hideToPlayer(UUID uuid){
        hiddenPlayers.putIfAbsent(this, new HashSet<>());
        Set<UUID> hidden = hiddenPlayers.get(this);

        hidden.add(uuid);
        //hide to children aswell
        getChildren().forEach(e -> e.hideToPlayer(uuid));
    }

    default Set<UUID> hiddenPlayers(){
        return hiddenPlayers.getOrDefault(this, new HashSet<>());
    }

    default void addChild(String name, CustomEntity entity){
        children.putIfAbsent(this, new HashMap<>());
        children.get(this).put(name, entity);
        entity.setParent(this);

        //catch child up on hidden players
        for(UUID uuid : hiddenPlayers.getOrDefault(this, new HashSet<>())){
            entity.hideToPlayer(uuid);
            entity.setInvertHide(invertHide.get(this));
        }
    }

    default CustomEntity getParent(){
        return parent.get(this);
    }

    default void setParent(CustomEntity parentEntity){
        parent.put(this, parentEntity);
    }

    default Collection<CustomEntity> getChildren(){
        Map<String, CustomEntity> childList = children.getOrDefault(this, new HashMap<>());
        return childList.values();
    }

    default boolean isSuspended(){
        return suspendedMap.getOrDefault(this, false);
    }

    default void setSuspended(boolean val){
        suspendedMap.put(this, val);
        getChildren().forEach(e -> e.setSuspended(val));
    }

    default void setInvertHide(boolean val){
        invertHide.put(this, val);
    }

    default boolean getInvertHide(){
        return invertHide.getOrDefault(this, false);
    }

    default boolean isDestroyed(){
        return isDestroyed.getOrDefault(this, false);
    }

    default boolean isDead(){
        return isDestroyed.getOrDefault(this, true);
    }

    default void setDead(boolean val){
        isDestroyed.put(this, val);
    }

    default boolean destroyOnUnload(){
        return destroyOnUnload.getOrDefault(this, false);
    }

    default void setDestroyOnUnload(boolean val){
        destroyOnUnload.put(this, val);
    }

    default boolean doNotDestroy(){
        return doNotDestroy.getOrDefault(this, false);
    }

    default void setDoNotDestroy(boolean val){
        doNotDestroy.put(this, val);
    }

    default boolean isReady(){
        return isReady.getOrDefault(this, false);
    }

    default void setIsReady(boolean val){
        isReady.put(this, val);
    }

    default Set<UUID> getDamagers(){
        return damagers.getOrDefault(this, new HashSet<>());
    }

    default void addDamager(UUID uuid){
        Set<UUID> damagersList = getDamagers();
        damagersList.add(uuid);
        damagers.put(this, damagersList);
    }

    default Pair<Location, Integer> getOrigin(){
        return originPoint.getOrDefault(this, null);
    }

    default void setOriginPoint(Location location, int maxDistance){
        Pair<Location, Integer> pair = Pair.of(location, maxDistance);
        originPoint.put(this, pair);
    }

    default <T> T getChild(String name){
        Map<String, CustomEntity> childList = children.getOrDefault(this, new HashMap<>());
        return (T) childList.get(name);
    }

    default void showIf(ToBooleanFunction<Player> consumer){
        showIfFunction.put(this, consumer);

    }
}












































