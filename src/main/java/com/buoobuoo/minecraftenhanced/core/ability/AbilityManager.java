package com.buoobuoo.minecraftenhanced.core.ability;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.ability.impl.BulwarkAbility;
import com.buoobuoo.minecraftenhanced.core.ability.impl.DashAbility;
import com.buoobuoo.minecraftenhanced.core.ability.impl.FlagellantAbility;
import com.buoobuoo.minecraftenhanced.core.ability.impl.SlashAbility;
import com.buoobuoo.minecraftenhanced.core.event.abilitycasttype.PlayerCritEvent;
import com.buoobuoo.minecraftenhanced.core.event.abilitycasttype.PlayerKillEnemyEvent;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateTickEvent;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.RangedWeapon;
import com.buoobuoo.minecraftenhanced.core.item.interfaces.type.Weapon;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AbilityManager implements Listener {
    private final List<Ability> abilityList = new ArrayList<>();

    private final MinecraftEnhanced plugin;

    public AbilityManager(MinecraftEnhanced plugin){
        this.plugin = plugin;

        registerAbilities(
                new SlashAbility(),
                new DashAbility(),
                new FlagellantAbility(),
                new BulwarkAbility()
        );
    }

    public void registerAbilities(Ability... abilities){
        for(Ability ability : abilities){
            abilityList.add(ability);
        }
    }

    public Ability getAbilityByID(String id){
        for(Ability ability : abilityList){
            if(ability.getId().equalsIgnoreCase(id))
                return ability;
        }
        return null;
    }

    public List<String> allAbilityID(){
        List<String> abilityIds = new ArrayList<>();
        for(Ability ability : abilityList){
            abilityIds.add(ability.getId());
        }
        return abilityIds;
    }

    public boolean playerHasCastType(ProfileData profileData, AbilityCastType type){
        if(profileData.getAbilityCastTypes() == null)
            return false;

        for(AbilityCastType t : profileData.getAbilityCastTypes()){
            if(t == null)
                continue;

            if(type == t)
                return true;
        }
        return false;
    }

    public List<Ability> getPlayerAbilities(ProfileData profileData){
        List<Ability> abilities = new ArrayList<>();
        for(String id : profileData.getAbilityIDs()){
            abilities.add(getAbilityByID(id));
        }
        return abilities;
    }

    public List<Ability> getAbilitiesByCastType(ProfileData profileData, AbilityCastType type) {
        List<Integer> slots = new ArrayList<>();
        List<Ability> ret = new ArrayList<>();
        List<Ability> playerAbilities = getPlayerAbilities(profileData);

        for (int i = 0; i < profileData.getAbilityCastTypes().length; i++) {
            AbilityCastType ct = profileData.getAbilityCastTypes()[i];
            if (ct == type)
                slots.add(i);
        }
        for (int i : slots) {
            ret.add(playerAbilities.get(i));
        }
        return ret;
    }

    public Entity getNearestEntity(Location loc, double radius){
        List<Entity> entityList = (List<Entity>)loc.getWorld().getNearbyEntities(loc, radius, radius, radius);
        return entityList.get(0);
    }

    public void castAblitiesByCastType(ProfileData profileData, AbilityCastType type){
        Player player = Bukkit.getPlayer(profileData.getOwnerID());
        for(Ability ability : getAbilitiesByCastType(profileData, type)){
            ability.cast(plugin, player, getNearestEntity(player.getLocation(), ability.getRadius()), type.getEffectiveness());
        }
    }

    public void castAblitiesByCastType(ProfileData profileData, AbilityCastType type, Entity target){
        Player player = Bukkit.getPlayer(profileData.getOwnerID());
        for(Ability ability : getAbilitiesByCastType(profileData, type)){
            ability.cast(plugin, player, target, type.getEffectiveness());
        }
    }

    public int getAbilityCoolDown(ProfileData profileData, Ability ability){
        String id = ability.getId();
        return profileData.getCooldowns().getOrDefault(id, 0);
    }

    public void setAbilityCoolDown(ProfileData profileData, Ability ability){
        setAbilityCoolDown(profileData, ability, ability.getAbilityCooldown());
    }

    public void setAbilityCoolDown(ProfileData profileData, Ability ability, int cooldownTicks){
        profileData.getCooldowns().put(ability.getId(), cooldownTicks);
    }

    public void addAbilityToPlayer(ProfileData profileData, Ability ability){
        String[] abilityIds = Util.setArrSize(profileData.getAbilityIDs(), 4);
        abilityIds = Util.insertEmptyStr(abilityIds, ability.getId());
        profileData.setAbilityIDs(abilityIds);
    }

    public boolean hasEmptyAbilitySlot(ProfileData profileData){
        int count = 0;
        for(Ability ability : getPlayerAbilities(profileData)){
            if(ability != null)
                count++;
        }
        return count < 4;
    }

    public boolean hasAbility(ProfileData profileData, Ability ability){
        return getPlayerAbilities(profileData).contains(ability);
    }


    @EventHandler
    public void onCrit(PlayerCritEvent event){
        Player player = event.getPlayer();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        if(!playerHasCastType(profileData, AbilityCastType.CAST_ON_CRIT))
            return;

        castAblitiesByCastType(profileData, AbilityCastType.CAST_ON_CRIT);
    }


    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(!(event.getDamager() instanceof Player))
            return;

        Player player = (Player)event.getDamager();

        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        if(!playerHasCastType(profileData, AbilityCastType.CAST_ON_HIT))
            return;

        Entity ent = event.getEntity();

        castAblitiesByCastType(profileData, AbilityCastType.CAST_ON_HIT, ent);
    }

    @EventHandler
    public void onDamageTaken(EntityDamageByEntityEvent event){

        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player)event.getEntity();

        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        if(!playerHasCastType(profileData, AbilityCastType.CAST_ON_HIT))
            return;

        Entity ent = event.getDamager();

        castAblitiesByCastType(profileData, AbilityCastType.CAST_ON_HIT, ent);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = (Player)event.getPlayer();

        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        if(!playerHasCastType(profileData, AbilityCastType.CAST_ON_MOVE))
            return;

        if(event.getFrom().distance(event.getTo()) == 0)
            return;

        castAblitiesByCastType(profileData, AbilityCastType.CAST_ON_MOVE);
    }

    @EventHandler
    public void onKill(PlayerKillEnemyEvent event){
        Player player = event.getPlayer();

        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        if(!playerHasCastType(profileData, AbilityCastType.CAST_ON_KILL))
            return;

        Entity ent = event.getEntity();

        castAblitiesByCastType(profileData, AbilityCastType.CAST_ON_KILL, ent);
    }


    @EventHandler
    public void onKill(PlayerDeathEvent event){
        Player player = event.getEntity();

        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        if(!playerHasCastType(profileData, AbilityCastType.CAST_ON_DEATH))
            return;

        castAblitiesByCastType(profileData, AbilityCastType.CAST_ON_DEATH);
    }

    static final int timeoutTicks = 100;
    private Map<UUID, String> abilityComboMap = new ConcurrentHashMap<>();
    private Map<UUID, Integer> timeoutMap = new ConcurrentHashMap<>();

    @EventHandler
    public void updateTicks(UpdateTickEvent event){
        for(Map.Entry<UUID, Integer> entry : timeoutMap.entrySet()){
            Integer ticks = entry.getValue();
            UUID uuid = entry.getKey();
            if(ticks >= timeoutTicks) {
                timeoutMap.remove(uuid);
                abilityComboMap.remove(uuid);
                continue;
            }
            timeoutMap.put(uuid, ticks+1);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();
        String currCombo = abilityComboMap.getOrDefault(player.getUniqueId(), "");

        ItemStack item = player.getInventory().getItemInMainHand();
        CustomItem handler = plugin.getCustomItemManager().getRegistry().getHandler(item);
        if(!(handler instanceof Weapon))
            return;

        Action action = event.getAction();
        if(handler instanceof RangedWeapon)
            action = inverseAction(action);

        if(currCombo.equals("") && (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK))
            return;

        if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){
            currCombo += "L";
        }else if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
            currCombo += "R";
        }

        player.sendMessage(currCombo);
        abilityComboMap.put(player.getUniqueId(), currCombo);
        timeoutMap.putIfAbsent(player.getUniqueId(), 0);

        if(currCombo.length() == 3){
            ProfileData profileData = plugin.getPlayerManager().getProfile(player);
            AbilityCastType type = switch (currCombo){
                case "RLR" -> AbilityCastType.RLR;
                case "RRR" -> AbilityCastType.RRR;
                case "RLL" -> AbilityCastType.RLL;
                case "RRL" -> AbilityCastType.RRL;
                default -> null;
            };

            abilityComboMap.remove(player.getUniqueId());
            timeoutMap.remove(player.getUniqueId());
            if(type != null) {
                castAblitiesByCastType(profileData, type);
            }
        }
    }

    private Action inverseAction(Action action){
        return switch (action){
            case RIGHT_CLICK_BLOCK -> Action.LEFT_CLICK_BLOCK;
            case RIGHT_CLICK_AIR -> Action.LEFT_CLICK_AIR;
            case LEFT_CLICK_BLOCK -> Action.RIGHT_CLICK_BLOCK;
            case LEFT_CLICK_AIR -> Action.RIGHT_CLICK_AIR;
            default -> action;
        };
    }

    //super inefficient
    @EventHandler
    public void cooldownHandler(UpdateTickEvent event){
        for(ProfileData profileData : plugin.getPlayerManager().getProfileDataMap().values()){
            HashMap<String, Integer> cooldowns = new HashMap<>();
            if(profileData.getCooldowns() == null) {
                profileData.setCooldowns(cooldowns);
                continue;
            }

            for(Map.Entry<String, Integer> entry : profileData.getCooldowns().entrySet()){
                if(entry.getValue() <= 0)
                    continue;

                cooldowns.put(entry.getKey(), entry.getValue()-1);
            }
            profileData.setCooldowns(cooldowns);
        }
    }

}





























