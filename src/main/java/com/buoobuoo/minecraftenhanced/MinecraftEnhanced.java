package com.buoobuoo.minecraftenhanced;

import com.buoobuoo.minecraftenhanced.command.CommandManager;
import com.buoobuoo.minecraftenhanced.core.block.CustomBlockManager;
import com.buoobuoo.minecraftenhanced.core.cinematic.SpectatorManager;
import com.buoobuoo.minecraftenhanced.core.damage.DamageManager;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueManager;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.event.DatabaseConnectEvent;
import com.buoobuoo.minecraftenhanced.core.event.listener.AnvilRenamePacketListener;
import com.buoobuoo.minecraftenhanced.core.event.listener.PlayerInteractNpcPacketListener;
import com.buoobuoo.minecraftenhanced.core.event.listener.mechanic.EntityDamageByEntityEventListener;
import com.buoobuoo.minecraftenhanced.core.event.listener.mechanic.EntityDamageEventListener;
import com.buoobuoo.minecraftenhanced.core.event.listener.mechanic.EntityRegainHealthEventListener;
import com.buoobuoo.minecraftenhanced.core.event.listener.mechanic.EntitySpawnEventListener;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateSecondEvent;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateTickEvent;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventoryManager;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.item.attributes.ItemAttributeManager;
import com.buoobuoo.minecraftenhanced.core.entity.npc.NpcManager;
import com.buoobuoo.minecraftenhanced.core.party.PartyManager;
import com.buoobuoo.minecraftenhanced.core.player.PlayerManager;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import com.buoobuoo.minecraftenhanced.permission.PermissionManager;
import com.buoobuoo.minecraftenhanced.persistence.MongoHook;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@Getter
public class MinecraftEnhanced extends JavaPlugin implements Listener{

    private MongoHook mongoHook;

    private ProtocolManager protocolManager;
    private CustomBlockManager customBlockManager;
    private CustomItemManager customItemManager;
    private CustomInventoryManager customInventoryManager;
    private SpectatorManager spectatorManager;
    private NpcManager npcManager;
    private PartyManager partyManager;
    private QuestManager questManager;
    private PlayerManager playerManager;
    private PermissionManager permissionManager;
    private DialogueManager dialogueManager;
    private ItemAttributeManager itemAttributeManager;
    private EntityManager entityManager;
    private DamageManager damageManager;

    private CommandManager commandManager;

    // ;(
    @Getter
    private static MinecraftEnhanced instance;

    @Override
    public void onEnable(){
        initManagers();
        initListeners();
        initTimers();

        instance = this;
    }

    @Override
    public void onDisable(){
        npcManager.cleanup();
        mongoHook.disable();
    }

    public void initManagers(){
        mongoHook = new MongoHook(this);

        protocolManager = ProtocolLibrary.getProtocolManager();
        customBlockManager = new CustomBlockManager(this);
        customItemManager = new CustomItemManager(this);
        customInventoryManager = new CustomInventoryManager(this);
        spectatorManager = new SpectatorManager(this);
        npcManager = new NpcManager(this);
        partyManager = new PartyManager(this);
        questManager = new QuestManager(this);
        playerManager = new PlayerManager(this);
        permissionManager = new PermissionManager(this);
        dialogueManager = new DialogueManager(this);
        itemAttributeManager = new ItemAttributeManager(this);
        entityManager = new EntityManager(this);
        damageManager = new DamageManager(this);

        commandManager = new CommandManager(this);
    }

    public void initListeners(){
        registerEvents(
                this,
                customBlockManager,
                customItemManager,
                customInventoryManager,
                npcManager,
                spectatorManager,
                playerManager,

                //mechanic listeners
                new EntityRegainHealthEventListener(this),
                new EntityDamageByEntityEventListener(this),
                new EntitySpawnEventListener(this),
                new EntityDamageEventListener(this)
        );

        //packet listener
        new PlayerInteractNpcPacketListener(this);
        new AnvilRenamePacketListener(this);
    }

    public void initTimers(){
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new UpdateSecondEvent());
            }

        }.runTaskTimer(this, 0, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new UpdateTickEvent());
            }

        }.runTaskTimer(this, 0, 1);

    }

    //TEMP
    @EventHandler
    public void itemDrop(PlayerDropItemEvent event){
        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();

        if(!customItemManager.getRegistry().isCustomItem(itemStack))
            return;

        item.setGravity(false);
        item.setVelocity(new Vector(0, 0, 0));

        item.setCustomName(itemStack.getItemMeta().getDisplayName());
        item.setCustomNameVisible(true);
    }

    @EventHandler
    public void dbConnect(DatabaseConnectEvent event){
        for(Player player : Bukkit.getOnlinePlayers()){
            playerManager.getProfile(player);
        }
    }

    public void registerEvents(Listener... listeners){
        for(Listener listener : listeners){
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
