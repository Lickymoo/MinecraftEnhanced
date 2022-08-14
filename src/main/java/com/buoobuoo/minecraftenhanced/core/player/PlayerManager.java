package com.buoobuoo.minecraftenhanced.core.player;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.entity.EntityStatInstance;
import com.buoobuoo.minecraftenhanced.core.entity.impl.util.TagEntity;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateSecondEvent;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateTickEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.impl.profile.ProfileInventory;
import com.buoobuoo.minecraftenhanced.core.player.tempmodifier.TemporaryStatModifier;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import com.buoobuoo.minecraftenhanced.core.quest.impl.act1.ACT1_MQ1;
import com.buoobuoo.minecraftenhanced.core.status.StatusEffect;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import com.buoobuoo.minecraftenhanced.core.vfx.particle.ParticleDirectory;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerManager implements Listener {
    private final MinecraftEnhanced plugin;

    public PlayerManager(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    private final Map<UUID, ProfileData> profileDataMap = new HashMap<>();
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerData getPlayer(Player player){
        return getPlayer(player.getUniqueId());
    }

    public PlayerData getPlayer(UUID uuid){
        if(playerDataMap.containsKey(uuid))
            return playerDataMap.get(uuid);

        PlayerData playerData = PlayerData.load(plugin, uuid);
        playerDataMap.put(uuid, playerData);

        return playerData;
    }

    public ProfileData getProfile(Player player){
        PlayerData data = getPlayer(player);
        return getProfile(data.getActiveProfileID());
    }

    public ProfileData getProfile(UUID uuid){
        if(profileDataMap.containsKey(uuid))
            return profileDataMap.get(uuid);

        ProfileData profileData = ProfileData.load(plugin, uuid);
        profileDataMap.put(uuid, profileData);

        return profileData;
    }

    public boolean hasActive(Player player){
        PlayerData playerData = getPlayer(player);
        return playerData.getActiveProfileID() != null;
    }

    //Rank tag


    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event){
        //Cache user data before join so they dont wait as long
        UUID uuid = event.getUniqueId();
        PlayerData data = getPlayer(uuid);
        data.setOwnerID(uuid);
    }

    @EventHandler(priority =  EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerData playerData = getPlayer(player);

        player.getInventory().clear();
        player.setHealth(20);

        //LOAD PLAYER INVENTORY
        Inventory inv = new ProfileInventory(plugin, player).getInventory();
        player.openInventory(inv);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        //save data
        PlayerData playerData = getPlayer(event.getPlayer());
        EntityManager entityManager = plugin.getEntityManager();
        if(playerData.getActiveProfileID() != null)
            entityManager.cleanUp(getProfile(playerData.getActiveProfileID()));

        playerData.save(plugin);

        //remove all old profiles
        for(UUID uuid : playerData.getProfileIDs()){
            removeProfile(uuid);
        }

        playerDataMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onClose(UpdateSecondEvent event){
        for(Player player : Bukkit.getOnlinePlayers()){
            PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
            if(playerData.getActiveProfileID() != null)
                continue;

            Inventory inv = player.getOpenInventory().getTopInventory();

            if(plugin.getCustomInventoryManager().getRegistry().getHandler(inv) == null){
                Inventory i = new ProfileInventory(plugin, player).getInventory();
                player.openInventory(i);
            }

        }
    }

    public boolean isProfileLoaded(UUID uuid){
        return profileDataMap.containsKey(uuid);
    }

    @EventHandler
    public void updateSecond(UpdateSecondEvent event){
        for(PlayerData data : playerDataMap.values()) {
            Player player = Bukkit.getPlayer(data.getOwnerID());

            if (player == null)
                continue;

            if (data.getActiveProfileID() == null) {
                player.getInventory().setItem(8, null);
            } else {
                if (player.getGameMode() == GameMode.SPECTATOR)
                    continue;

                ProfileData profileData = getProfile(data.getActiveProfileID());
                EntityStatInstance instance = new EntityStatInstance(plugin, profileData);
                for(TemporaryStatModifier statModifier : profileData.getTemporaryStatModifiers()){
                    statModifier.getInstanceConsumer().accept(instance);
                    profileData.getTemporaryStatModifiers().remove(statModifier);
                }

                profileData.setStatInstance(instance);

                //regen
                double mana = profileData.getMana();
                double health = profileData.getHealth();
                profileData.setMana(Math.min(mana + instance.getManaRegenPS(), instance.getMaxMana()));
                profileData.setHealth(Math.min(health + instance.getHealthRegenPS(), instance.getMaxHealth()));
            }
        }
    }

    private String statusBar = "";

    @EventHandler
    public void updateTick(UpdateTickEvent event){
        for(PlayerData data : playerDataMap.values()){
            Player player = Bukkit.getPlayer(data.getOwnerID());
            if(player == null)
                continue;

            if(data.getActiveProfileID() == null) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
                continue;
            }

            if(player.getGameMode() == GameMode.SPECTATOR)
                continue;

            setDisplayPrefix(data);
            ProfileData profileData = getProfile(data.getActiveProfileID());
            if(profileData.getStatInstance() == null)
                profileData.setStatInstance(new EntityStatInstance(plugin, profileData));


            //Status effect bar
            statusBar = UnicodeSpaceUtil.getNeg(1);

            int maxStatusEffectIcons = 20;
            int iconsIndex = 0;
            for(StatusEffect effect : profileData.getStatusEffects()){
                statusBar += effect.getIcon();
                statusBar += UnicodeSpaceUtil.getNeg(1);

                iconsIndex++;
            }

            statusBar += UnicodeSpaceUtil.getPos(1 + (9 * (maxStatusEffectIcons - iconsIndex)) - 76);

            checkHealth(player, profileData);
            checkMana(player, profileData);
            checkLevel(player, profileData);

            player.setWalkSpeed(Math.max(0, (float) profileData.getStatInstance().getWalkSpeed()));

            if(player.getGameMode() == GameMode.SURVIVAL)
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(statusBar));

            player.setFoodLevel(20);
            checkStart(profileData);
        }
    }

    private void checkLevel(Player player, ProfileData profileData){
        player.setLevel(profileData.getLevel());

        int level = profileData.getLevel();
        int requiredExp = 25 * level * (1 + level);
        int currentExp = profileData.getExperience();

        if(currentExp >= requiredExp){
            ParticleDirectory.LEVELUP.playEffect(plugin, player.getLocation(), 1, 2.5, 3);
            profileData.setLevel(level+1);
            profileData.setExperience(currentExp-requiredExp);

            EntityManager entityManager = plugin.getEntityManager();
            List<TagEntity> entityList = entityManager.spawnHologram(player.getLocation().clone().add(0, 1.5, 0), 40, CharRepo.TAG_LEVEL_UP.getCh());

            new BukkitRunnable() {
                @Override
                public void run() {
                    for(TagEntity ent : entityList){
                        if(ent == null)
                            this.cancel();

                        ArmorStand as = (ArmorStand) ent.asEntity().getBukkitEntity();
                        Location loc = as.getLocation();
                        loc.add(0, .05, 0);
                        as.teleport(loc);
                    }
                }

            }.runTaskTimer(plugin, 0, 1);
        }

        float progress = Util.clamp((float)currentExp / (float)requiredExp, 0, 1);
        player.setExp(progress);
    }

    private void checkMana(Player player, ProfileData profileData) {

        double mana = profileData.getMana();

        double maxMana = profileData.getStatInstance().getMaxMana();

        if(mana > maxMana){
            mana = maxMana;
            profileData.setMana(mana);
        }

        double val = Math.floor((mana / maxMana) * 20d) / 2;

        double fullBalls = Math.floor(val);
        double halfBalls = Math.ceil(val - Math.floor(val));
        double emptyBalls = 10 - (fullBalls + halfBalls);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < emptyBalls; i++) {
            sb.append(UnicodeSpaceUtil.getNeg(2));
            sb.append(CharRepo.MANA_BALL_EMPTY);
        }
        for (int i = 0; i < halfBalls; i++) {
            sb.append(UnicodeSpaceUtil.getNeg(2));
            sb.append(CharRepo.MANA_BALL_HALF);
        }
        for (int i = 0; i < fullBalls; i++) {
            sb.append(UnicodeSpaceUtil.getNeg(2));
            sb.append(CharRepo.MANA_BALL_FULL);
        }

        if (player.getGameMode() == GameMode.SURVIVAL) {
            statusBar += sb.toString();
        }else{
            statusBar += "";
        }
    }

    private void checkHealth(Player player, ProfileData profileData) {
        double health = profileData.getHealth();

        double maxHealth = profileData.getStatInstance().getMaxHealth();

        //DEATH
        if (health <= 0) {
            player.setHealth(0);
            profileData.setHealth(maxHealth);
            profileData.setMana(ProfileData.BASE_MANA);
            return;
        }
        if(health > maxHealth){
            health = maxHealth;
            profileData.setHealth(maxHealth);
        }

        if (!player.isDead()) {
            player.setHealth((health / maxHealth) * 20);
        }

    }

    private void setDisplayPrefix(PlayerData playerData) {
        Player player = Bukkit.getPlayer(playerData.getOwnerID());

        if(playerData.getActiveProfileID() == null)
            return;

        ProfileData profileData = getProfile(player);
        try {
            Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
            Team team = scoreboard.getTeam(player.getName());


            if(team == null)
                team = scoreboard.registerNewTeam(player.getName());

            String prefix = CharRepo.numToTagString(profileData.getLevel());
            prefix = prefix + (playerData.getGroup().getGroupPrefix() != null ? playerData.getGroup().getGroupPrefix() + " " : "");
            team.setPrefix(Util.formatColour(prefix));
            team.addEntry(player.getName());

            Objective objective = null;

            if(scoreboard.getObjective("showhealth") == null)
                objective = scoreboard.registerNewObjective("showhealth", "dummy", "");
            else
                objective = scoreboard.getObjective("showhealth");

            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName(CharRepo.HEART.getCh());
            objective.getScore(player.getName()).setScore((int)profileData.getHealth());


        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void removeProfile(UUID uuid){
        profileDataMap.remove(uuid);
    }

    public void saveAll(){
        for(PlayerData playerData : playerDataMap.values()){
            playerData.save(plugin);
        }
    }

    public boolean tempStatModifiersHasClass(ProfileData profileData, Class<?> clazz){
        for(TemporaryStatModifier stat : profileData.getTemporaryStatModifiers()){
            if(stat.getModifyingClass() == clazz)
                return true;
        }
        return false;
    }

    public void checkStart(ProfileData profileData){
        //check if player has started

        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        QuestManager questManager = plugin.getQuestManager();
        if(!questManager.hasActiveQuest(ACT1_MQ1.class, profileData) && !questManager.hasCompletedQuest(ACT1_MQ1.class, profileData)){
            beginStory(profileData);
        }
    }

    public void beginStory(ProfileData profileData){

        QuestManager questManager = plugin.getQuestManager();
        questManager.startQuest(ACT1_MQ1.class, profileData);


    }
}









































