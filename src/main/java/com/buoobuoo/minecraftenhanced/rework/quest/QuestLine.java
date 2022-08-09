package com.buoobuoo.minecraftenhanced.rework.quest;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.NpcEntity;
import com.buoobuoo.minecraftenhanced.core.event.DialogueNextEvent;
import com.buoobuoo.minecraftenhanced.core.event.PlayerInteractNpcEvent;
import com.buoobuoo.minecraftenhanced.core.event.RouteFinishEvent;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateTickEvent;
import com.buoobuoo.minecraftenhanced.core.navigation.Route;
import com.buoobuoo.minecraftenhanced.core.navigation.RouteSingularPlayer;
import com.buoobuoo.minecraftenhanced.core.quest.Quest;
import com.buoobuoo.minecraftenhanced.core.util.JSONUtil;
import com.buoobuoo.minecraftenhanced.core.util.Pair;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Consumer;

import java.util.*;

public abstract class QuestLine implements Listener {

    private final MinecraftEnhanced plugin;
    protected final String questName;
    protected final String questID;

                              //index  ,      delay  , runnable
    private final LinkedHashMap<Integer, Pair<Integer, Consumer<Player>>> stepMap = new LinkedHashMap<>();

                    //name          player  , isdone
    private final Map<String, Map<UUID, Boolean>> determinantMap = new HashMap<>();

                  //player, index
    private final Map<UUID, Integer> instancePointer = new HashMap<>();

    private int builderPointer = 0;

    //meta
    private final Map<Class<? extends NpcEntity>, String> npcInteractMap = new HashMap<>();
    private final Map<String, Object> objectMap = new HashMap<>();


    public QuestLine(MinecraftEnhanced plugin, String questName, String questID){
        this.questName = questName;
        this.questID = questID;
        this.plugin = plugin;
    }

    public void setDeterminant(Player player, String determinant, boolean value){
        determinantMap.putIfAbsent(determinant, new HashMap<>());
        determinantMap.get(determinant).put(player.getUniqueId(), value);
    }

    private void addStep(Consumer<Player> consumer){
        Pair<Integer, Consumer<Player>> step = Pair.of(0, consumer);
        stepMap.put(builderPointer++, step);
    }

    public void start(Player player){
        instancePointer.put(player.getUniqueId(), 0);
    }

    public void next(Player player){
        int index = instancePointer.getOrDefault(player.getUniqueId(), 0);
        instancePointer.put(player.getUniqueId(), index+1);
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
        int index = instancePointer.getOrDefault(player.getUniqueId(), 0);

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
        determinantMap.putIfAbsent(determinant, new HashMap<>());
        Consumer<Player> consumer = player -> {
            UUID uuid = player.getUniqueId();
            Map<UUID, Boolean> pair = determinantMap.getOrDefault(determinant, new HashMap<>());

            if(pair.getOrDefault(uuid, false)) {
                next(player);

                //reset incase of multiple determinant usecase
                pair.put(uuid, false);
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

    public QuestLine dialogue(CharRepo icon, String... text){
        Consumer<Player> consumer = player -> {
            player.sendMessage(text);
            next(player);
        };
        addStep(consumer);
        return this;
    }

    public QuestLine dialogueNext(CharRepo icon, String... text){
        String id = UUID.randomUUID().toString();
        Consumer<Player> consumer = player -> {
            player.sendMessage(text);
            player.spigot().sendMessage(JSONUtil.getJSON(Util.formatColour("&a&l> Next"), "diagnext " + id, false, ""));
            next(player);
        };
        addStep(consumer);
        when(id);
        return this;
    }

    public QuestLine whenNpcInteract(Class<? extends NpcEntity> entClass){
        String id = UUID.randomUUID().toString();
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

    public QuestLine finish(){
        Consumer<Player> consumer = player -> {
            instancePointer.remove(player.getUniqueId());
            Bukkit.broadcastMessage("FINISH");
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

    //listener
    @EventHandler
    public void onTick(UpdateTickEvent event){
        for(UUID uuid : instancePointer.keySet()){
            Player player = Bukkit.getPlayer(uuid);
            executePointer(player);
        }
    }

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
}
































