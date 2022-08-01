package com.buoobuoo.minecraftenhanced;

import com.buoobuoo.minecraftenhanced.core.block.CustomBlockManager;
import com.buoobuoo.minecraftenhanced.core.cinematic.SpectatorManager;
import com.buoobuoo.minecraftenhanced.core.command.CommandManager;
import com.buoobuoo.minecraftenhanced.core.dialogue.DialogueManager;
import com.buoobuoo.minecraftenhanced.core.event.DatabaseConnectEvent;
import com.buoobuoo.minecraftenhanced.core.event.UpdateSecondEvent;
import com.buoobuoo.minecraftenhanced.core.event.listener.AnvilRenamePacketListener;
import com.buoobuoo.minecraftenhanced.core.event.listener.PlayerInteractNpcPacketListener;
import com.buoobuoo.minecraftenhanced.core.inventory.CustomInventoryManager;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.npc.NpcManager;
import com.buoobuoo.minecraftenhanced.core.party.PartyManager;
import com.buoobuoo.minecraftenhanced.core.permission.PermissionManager;
import com.buoobuoo.minecraftenhanced.core.player.PlayerManager;
import com.buoobuoo.minecraftenhanced.core.quest.QuestManager;
import com.buoobuoo.minecraftenhanced.persistence.MongoHook;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
        playerManager.saveAll();
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
                playerManager
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

    }

    @EventHandler
    public void dbConnect(DatabaseConnectEvent event){
        for(Player player : Bukkit.getOnlinePlayers()){
            playerManager.getPlayer(player);
        }
    }

    public void registerEvents(Listener... listeners){
        for(Listener listener : listeners){
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
