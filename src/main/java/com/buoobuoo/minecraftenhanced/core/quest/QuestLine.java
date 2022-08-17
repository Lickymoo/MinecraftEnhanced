package com.buoobuoo.minecraftenhanced.core.quest;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.area.Area;
import com.buoobuoo.minecraftenhanced.core.chat.ChatRestrictionMode;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.NpcEntity;
import com.buoobuoo.minecraftenhanced.core.event.*;
import com.buoobuoo.minecraftenhanced.core.item.CustomItem;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.navigation.RouteSingularPlayer;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.quest.util.CinematicConsumer;
import com.buoobuoo.minecraftenhanced.core.util.JSONUtil;
import com.buoobuoo.minecraftenhanced.core.util.Pair;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.vfx.cinematic.CinematicSequence;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class QuestLine implements Listener {

    private final MinecraftEnhanced plugin;
    protected final String questName;
    protected final String questID;
    protected final String questBrief;

                              //index  ,      delay  , runnable
    private final LinkedHashMap<Integer, Pair<Integer, Consumer<Player>>> stepMap = new LinkedHashMap<>();

                    //name          player  , isdone
    private final Map<String, ConcurrentHashMap<UUID, Boolean>> determinantMap = new HashMap<>();
    private final Map<String, Consumer<Player>> wheneverDeterminantMap = new HashMap<>();

                  //player, index
    private final Map<UUID, Integer> instancePointer = new HashMap<>();

                     //index
    private final List<Integer> checkPoints = new ArrayList<>();
    private final Map<String, Integer> markers = new HashMap<>();

    private int builderPointer = 0;

    //meta
    private final Map<Class<? extends NpcEntity>, String> npcInteractMap = new HashMap<>();
    private final Map<Class<? extends CustomEntity>, Map<UUID, String>> customEntityKillMap = new HashMap<>();
    private final Map<Class<? extends CustomItem>, Map<UUID, String>> customItemPickupMap = new HashMap<>();
    private final Map<Class<? extends Area>, Map<UUID, String>> areaEnterMap = new HashMap<>();
    private final Map<String, Object> objectMap = new HashMap<>();

    public QuestLine(MinecraftEnhanced plugin, String questName, String questID, String questBrief){
        this.questName = questName;
        this.questID = questID;
        this.plugin = plugin;
        this.questBrief = questBrief;
    }

    public void loadQuestString(Player player, String str){
        //QUESTID:INDEX:extra1:extra2:extra3
        String[] data = str.split(":");
        String questID = data[0];
        int index = Integer.parseInt(data[1]);

        start(player, index);
    }

    public String saveQuestString(Player player){
        //QUESTID:INDEX:extra1:extra2:extra3
        StringBuilder sb = new StringBuilder();
        sb.append(questID);
        sb.append(":");
        sb.append(getCurrentIndex(player));
        return sb.toString();
    }

    public void checkpoint(){
        checkPoints.add(builderPointer-1);
    }

    public void checkpoint(int index){
        checkPoints.add(0);
    }

    public void checkpoint(String dialogue){
        checkpoint();
        dialogue(CharRepo.UI_PORTRAIT_QUEST_CHECKPOINT, "&7&lQuest Checkpoint reached - \n &r" + dialogue);
    }

    public int getLastCheckPointFrom(int index){
        int checkPoint = 0;
        for(int i : checkPoints){
            if(i > checkPoint && i <= index)
                checkPoint = i;
        }
        return checkPoint;
    }

    public void marker(String marker){
        markers.put(marker, builderPointer-1);
    }

    public boolean isBeforeMarker(Player player, String marker){
        if(!markers.containsKey(marker))
            return false;

        int index = getCurrentIndex(player);
        int markerIndex = markers.get(marker);

        if(index < markerIndex)
            return true;

        return false;
    }


    public void setDeterminant(Player player, String determinant, boolean value){
        if(wheneverDeterminantMap.containsKey(determinant)){
            wheneverDeterminantMap.get(determinant).accept(player);
            return;
        }

        determinantMap.putIfAbsent(determinant, new ConcurrentHashMap<>());
        determinantMap.get(determinant).put(player.getUniqueId(), value);
        executePointer(player);
        determinantMap.get(determinant).put(player.getUniqueId(), false);
    }

    private void addStep(Consumer<Player> consumer){
        Pair<Integer, Consumer<Player>> step = Pair.of(0, consumer);
        stepMap.put(builderPointer++, step);
    }

    public void start(Player player){
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        QuestManager questManager = plugin.getQuestManager();
        if(questManager.hasCompletedQuest(this.getClass(), profileData))
            return;

        instancePointer.put(player.getUniqueId(), 0);
        executePointer(player);

        plugin.getQuestManager().applyActiveToProfile(this.getClass(), profileData);
    }

    public void start(Player player, int index){
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        QuestManager questManager = plugin.getQuestManager();
        if(questManager.hasCompletedQuest(this.getClass(), profileData))
            return;

        instancePointer.put(player.getUniqueId(), getLastCheckPointFrom(index));
        executePointer(player);

        //find last checkpoint
    }

    public void next(Player player){
        int index = instancePointer.getOrDefault(player.getUniqueId(), 0);
        if(!stepMap.containsKey(index))
            return;

        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        QuestManager questManager = plugin.getQuestManager();

        if(questManager.hasCompletedQuest(this.getClass(), profileData) || getCurrentIndex(player) > index)
            return;

        plugin.getQuestManager().applyActiveToProfile(this.getClass(), profileData);

        instancePointer.put(player.getUniqueId(), index+1);
        executePointer(player);
    }

    public void nextSafe(Player player, int i){
        int index = instancePointer.getOrDefault(player.getUniqueId(), 0);
        if(index != i)
            return;

        instancePointer.put(player.getUniqueId(), index+1);
    }

    public int getCurrentIndex(Player player){
        return instancePointer.getOrDefault(player.getUniqueId(), 0);
    }

    public void executePointer(Player player){
        int index = instancePointer.getOrDefault(player.getUniqueId(), -1);
        if(index == -1)
            return;

        Consumer<Player> consumer = stepMap.get(index).getRight();

        consumer.accept(player);
    }

    public QuestLine delay(int delay){
        return executeOverride(player -> {
            Bukkit.getScheduler().runTaskLater(plugin, pl -> {
                next(player);
            }, delay);
        });
    }

    public QuestLine when(String determinant){
        determinantMap.putIfAbsent(determinant, new ConcurrentHashMap<>());
        Consumer<Player> consumer = player -> {
            UUID uuid = player.getUniqueId();
            Map<UUID, Boolean> pair = determinantMap.get(determinant);

            pair.putIfAbsent(uuid, false);

            if(pair.get(uuid)) {
                next(player);
            }
        };
        addStep(consumer);
        return this;
    }

    public QuestLine executeOverride(Consumer<Player> consumer){
        Consumer<Player> outConsumer = player -> {
            consumer.accept(player);
        };
        addStep(outConsumer);
        return this;
    }

    public QuestLine execute(Consumer<Player> consumer){
        //wrap with next
        Consumer<Player> outConsumer = player -> {
            consumer.accept(player);
            next(player);
        };
        addStep(outConsumer);
        return this;
    }

    public QuestLine dialogue(CharRepo icon, String text){
        Consumer<Player> consumer = player -> {
            Util.sendDialogueBox(player, icon, text);
            next(player);
        };
        addStep(consumer);
        return this;
    }

    public QuestLine dialogue(CharRepo icon, String text, int delay){
        String id = UUID.randomUUID().toString();
        execute(player -> {
            plugin.getChatManager().setChatMode(player, ChatRestrictionMode.DIALOGUE_RESTRICTION);
        });
        Consumer<Player> consumer = player -> {
            Util.sendDialogueBox(player, icon, text);
        };
        execute(consumer);
        delay(delay);
        execute(player -> {
            plugin.getChatManager().setChatMode(player, ChatRestrictionMode.UNRESTRICTED);
        });
        return this;
    }

    public QuestLine dialogueNext(CharRepo icon, String text){
        String id = UUID.randomUUID().toString();
        execute(player -> {
            plugin.getChatManager().setChatMode(player, ChatRestrictionMode.DIALOGUE_RESTRICTION);
        });
        Consumer<Player> consumer = player -> {
            TextComponent nextButton = JSONUtil.getJSON(Util.formatColour("             &a&l> Next"), "diagnext " + id, false, "");
            Util.sendDialogueBox(player, icon, text, nextButton);
            next(player);
        };
        addStep(consumer);
        when(id);
        execute(player -> {
            plugin.getChatManager().setChatMode(player, ChatRestrictionMode.UNRESTRICTED);
        });
        return this;
    }

    public QuestLine whenNpcInteract(Class<? extends NpcEntity> entClass){
        String id = npcInteractMap.getOrDefault(entClass, UUID.randomUUID().toString());
        npcInteractMap.put(entClass, id);
        when(id);
        return this;
    }

    public QuestLine whenRouteComplete(RouteSingularPlayer route){
        String id = UUID.randomUUID().toString();
        Consumer<Player> consumer = player -> {
            route.setPlayer(player);
            route.setRouteID(id);
        };
        execute(consumer);
        when(id);
        return this;
    }

    public QuestLine whenRouteComplete(String routeName){
        String id = UUID.randomUUID().toString();
        Consumer<Player> consumer = player -> {
            RouteSingularPlayer route = getObject(RouteSingularPlayer.class, routeName, player);
            route.setPlayer(player);
            route.setRouteID(id);
        };
        execute(consumer);
        when(id);
        return this;
    }

    public QuestLine whenKillEntity(Class<? extends CustomEntity> entityClass){
        customEntityKillMap.putIfAbsent(entityClass, new HashMap<>());
        String id = UUID.randomUUID().toString();
        Consumer<Player> consumer = player -> {
            Map<UUID, String> map = customEntityKillMap.get(entityClass);
            map.put(player.getUniqueId(), id);
        };
        execute(consumer);
        when(id);
        return this;
    }

    public QuestLine whenPickupCustomItem(Class<? extends CustomItem> itemClass){
        customItemPickupMap.putIfAbsent(itemClass, new HashMap<>());
        String id = UUID.randomUUID().toString();

        Consumer<Player> consumer = player -> {
            Map<UUID, String> map = customItemPickupMap.get(itemClass);
            map.put(player.getUniqueId(), id);
        };
        execute(consumer);
        when(id);

        return this;
    }

    public QuestLine whenEnterArea(Class<? extends Area> areaClass){
        areaEnterMap.putIfAbsent(areaClass, new HashMap<>());
        String id = UUID.randomUUID().toString();

        Consumer<Player> consumer = player -> {
            Map<UUID, String> map = areaEnterMap.get(areaClass);
            map.put(player.getUniqueId(), id);
        };
        execute(consumer);
        when(id);
        return this;
    }

    public QuestLine cinematic(CinematicSequence sequence){
        sequence.onFinish = this::next;
        Consumer<Player> consumer = sequence::execute;
        execute(consumer);
        return this;
    }

    public QuestLine cinematic(CinematicConsumer cons){
        execute(player -> cons.accept(player).execute(player));
        return this;
    }

    public QuestLine whenever(String determinant, Consumer<Player> player){
        wheneverDeterminantMap.put(determinant, player);
        return this;
    }

    public QuestLine startOtherQuest(Class<? extends QuestLine> questClass){
        QuestManager questManager = plugin.getQuestManager();
        Consumer<Player> consumer = player -> {
            ProfileData profileData = plugin.getPlayerManager().getProfile(player);
            questManager.startQuest(questClass, profileData);
            next(player);
        };
        execute(consumer);
        return this;
    }

    public QuestLine finish(){
        Consumer<Player> consumer = player -> {
            instancePointer.remove(player.getUniqueId());
            ProfileData profileData = plugin.getPlayerManager().getProfile(player);
            plugin.getQuestManager().finishQuest(this.getClass(), profileData);
            //finish
            Util.sendDialogueBox(player, CharRepo.UI_PORTRAIT_QUEST_COMPLETE, "&7&lQuest Complete - &f&l" + getQuestName());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
        };
        addStep(consumer);
        return this;
    }

    public QuestLine finish(Class<? extends QuestLine>... beginQuest){
        Consumer<Player> consumer = player -> {
            QuestManager questManager = plugin.getQuestManager();
            instancePointer.remove(player.getUniqueId());
            ProfileData profileData = plugin.getPlayerManager().getProfile(player);
            questManager.finishQuest(this.getClass(), profileData);
            //finish
            StringBuilder sb = new StringBuilder("&7&lQuest Complete - &f&l" + getQuestName());
            for(Class<? extends QuestLine> questLine : beginQuest) {
                QuestLine newQuest = questManager.getQuestByClass(questLine);
                questManager.startQuest(questLine, profileData);
                sb.append(" \n&7New Quest - ");
                sb.append(newQuest.getQuestName());
            }

            Util.sendDialogueBox(player, CharRepo.UI_PORTRAIT_QUEST_COMPLETE, sb.toString());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
        };
        addStep(consumer);
        return this;
    }


    public QuestLine finish(boolean silent){
        if(!silent){
            return finish();
        }

        Consumer<Player> consumer = player -> {
            instancePointer.remove(player.getUniqueId());
            ProfileData profileData = plugin.getPlayerManager().getProfile(player);
            plugin.getQuestManager().finishQuest(this.getClass(), profileData);
            //finish
        };
        addStep(consumer);
        return this;
    }

    public QuestLine putObject(String tag, Object object, Player player){
        objectMap.put(player.getUniqueId().toString() + tag, object);
        return this;
    }

    public <T> T getObject(Class<? extends T> clazz, String tag, Player player){
        return (T)objectMap.get(player.getUniqueId().toString() + tag);
    }

    public boolean isApplicable(Player player){
        if(!plugin.getPlayerManager().hasActive(player))
            return false;

        QuestManager questManager = plugin.getQuestManager();
        ProfileData profileData = plugin.getPlayerManager().getProfile(player);
        return questManager.hasActiveQuest(this.getClass(), profileData);
    }

    //player
    @EventHandler
    public void dialogueNextEvent(DialogueNextEvent event){
        if(!determinantMap.containsKey(event.getId()))
            return;

        Player player = event.getPlayer();
        setDeterminant(player, event.getId(), true);
    }

    @EventHandler
    public void npcInteract(PlayerInteractNpcEvent event){
        NpcEntity handler = event.getHandler();
        Player player = event.getPlayer();

        for(Map.Entry<Class<? extends NpcEntity>, String> entry : npcInteractMap.entrySet()) {
            if (handler.getClass() == entry.getKey()){
                setDeterminant(player, entry.getValue(), true);
                return;
            }
        }
    }

    @EventHandler
    public void routeFinish(RouteFinishEvent event){
        if(!(event.getRoute() instanceof RouteSingularPlayer))
            return;

        RouteSingularPlayer routeSingularPlayer = (RouteSingularPlayer) event.getRoute();
        setDeterminant(routeSingularPlayer.getPlayer(), routeSingularPlayer.getRouteID(), true);
    }

    @EventHandler
    public void onCustomEntityKill(CustomEntityKillByPlayerEvent event){
        CustomEntity ent = event.getEntity();
        Player player = event.getPlayer();
        Map<UUID, String> map = customEntityKillMap.getOrDefault(ent.getClass(), new HashMap<>());
        if(!isApplicable(player))
            return;

        if(map.containsKey(player.getUniqueId()))
            setDeterminant(player, map.get(player.getUniqueId()), true);
    }

    @EventHandler
    public void onCustomItemPickup(PlayerPickupItemEvent event){
        ItemStack item = event.getItem().getItemStack();

        CustomItemManager itemManager = plugin.getCustomItemManager();
        CustomItem customItem = itemManager.getRegistry().getHandler(item);
        if(customItem == null)
            return;

        Player player = event.getPlayer();
        Map<UUID, String> map = customItemPickupMap.getOrDefault(customItem.getClass(), new HashMap<>());
        if(!isApplicable(player))
            return;

        if(map.containsKey(player.getUniqueId()))
            setDeterminant(player, map.get(player.getUniqueId()), true);
    }

    @EventHandler
    public void onEnterArea(AreaEnterEvent event){
        Player player = event.getPlayer();
        if(!isApplicable(player))
            return;

       Map<UUID, String> map = areaEnterMap.getOrDefault(event.getArea().getClass(), new HashMap<>());
        if(map.containsKey(player.getUniqueId()))
            setDeterminant(player, map.get(player.getUniqueId()), true);
    }

    public void clearPlayer(Player player){
        UUID uuid = player.getUniqueId();
        instancePointer.remove(uuid);
        for(Map<UUID, Boolean> map : determinantMap.values()){
            map.remove(uuid);
        }
    }
}
































