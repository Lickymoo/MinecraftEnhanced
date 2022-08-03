package com.buoobuoo.minecraftenhanced.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.item.CustomItems;
import com.buoobuoo.minecraftenhanced.core.npc.NpcInstance;
import com.buoobuoo.minecraftenhanced.core.npc.Npcs;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

@CommandAlias("enh")
public class EnhCommand extends BaseCommand {

    private final MinecraftEnhanced plugin;

    public EnhCommand(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @Subcommand("give")
    @CommandCompletion("@custom-items")
    public void give(Player player, CustomItems item){
        ItemStack stack = plugin.getCustomItemManager().getItem(item);

        player.getInventory().addItem(stack);
    }

    @Subcommand("give")
    @CommandCompletion("@custom-items")
    public void give(Player player, CustomItems item, int amount){
        ItemStack stack = plugin.getCustomItemManager().getItem(item);
        stack.setAmount(amount);

        player.getInventory().addItem(stack);
    }

    @Subcommand("npc spawn")
    @CommandCompletion("@npcs")
    public void spawn(Player player, Npcs npc){
        Location loc = player.getLocation();

        NpcInstance pni = new NpcInstance(plugin, npc.getHandler(), loc, player);
        pni.spawn();

        player.sendMessage("Successfully spawned npc (Ent ID: " + pni.getEntity().getId() + ", Instance ID: " + pni.getId() + ")");
    }

    @Subcommand("npc spawn")
    @CommandCompletion("@npcs @players")
    public void spawn(Player player, Npcs npc, OnlinePlayer... players){
        Location loc = player.getLocation();

        Player[] bPlayers = new Player[players.length];
        for(int i = 0; i < players.length; i++){
            bPlayers[i] = players[i].getPlayer();
        }

        NpcInstance pni = new NpcInstance(plugin, npc.getHandler(), loc, bPlayers);
        pni.spawn();

        player.sendMessage("Successfully spawned npc (Ent ID: " + pni.getEntity().getId() + ", Instance ID: " + pni.getId() + ")");
    }

    @Subcommand("npc spawn all")
    @CommandCompletion("@npcs")
    public void spawnGroup(Player player, Npcs npc){
        Location loc = player.getLocation();

        NpcInstance pni = new NpcInstance(plugin, npc.getHandler(), loc, Bukkit.getOnlinePlayers().toArray(new Player[0]));
        pni.spawn();

        player.sendMessage("Successfully spawned npc (Ent ID: " + pni.getEntity().getId() + ", Instance ID: " + pni.getId() + ")");
    }

    @Subcommand("npc destroy ent")
    public void destroyEnt(Player player, int id){
        Location loc = player.getLocation();

        NpcInstance pni = plugin.getNpcManager().getInstanceByEntityID(id);
        pni.destroy();

        player.sendMessage("Successfully destroyed npc: (Ent ID: " + id + ")");
    }

    @Subcommand("npc destroy inst")
    public void destroyInst(Player player, int id){
        Location loc = player.getLocation();

        NpcInstance pni = plugin.getNpcManager().getInstanceByInstID(id);
        pni.destroy();

        player.sendMessage("Successfully destroyed npc: (Inst ID: " + id + ")");
    }

    @Subcommand("npc move ent")
    public void moveEnt(Player player, int id){
        Location loc = player.getLocation();

        NpcInstance pni = plugin.getNpcManager().getInstanceByEntityID(id);
        pni.moveEntityTo(loc.getX(), loc.getY(), loc.getZ());
    }

    @Subcommand("npc move inst")
    public void moveInst(Player player, int id){
        Location loc = player.getLocation();

        NpcInstance pni = plugin.getNpcManager().getInstanceByInstID(id);
        pni.moveEntityTo(loc.getX(), loc.getY(), loc.getZ());
    }


    //test cmds
    @Subcommand("intro")
    public void intro(Player player){
        Location loc = player.getLocation();
        Location npcLoc = loc.clone().add(1, 0 , 3);

        loc.setPitch(0);
        loc.setYaw(0);

        NpcInstance pni = new NpcInstance(plugin, Npcs.Jayden.getHandler(), npcLoc, player);
        pni.spawn();

        plugin.getSpectatorManager().viewLocNoVignette(player, loc.add(0, 1, 0));
        player.sendTitle("", "", 0, 10000, 0);
    }

    @Subcommand("skull")
    public void skull(Player player){
        ItemStack item = new ItemBuilder(Material.PLAYER_HEAD).setCustomModelData(10)
                .skullTexture(
                        "ewogICJ0aW1lc3RhbXAiIDogMTY1OTE0MDUwMTA3MywKICAicHJvZmlsZUlkIiA6ICIzYWJkYjI5ZDI2MTU0YTAxOWEzZWQ3OGRlMzI4OWUxNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJFcmljSHViZXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWMzMWRlODIxZWE5NDAzNjJmZjI1MjM1N2UwYWQ0MDU5MjcyZWE0ZjU5ZTJmYjdiZTJlMDYyNmU5YjM3YzU3YSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                        "upQxiwgQsAEoEFxzi0JNKjOiZXh+lBcL5oqg31VzVw2m6jd2HMxCfRnlm4FX6yqstNGHQoAK9dCpDrUWLuyG1DxNojHhlbg8DJpZUv/nsMK/VhaUIUiA5jQUeRQZM8QItWq4iMWUjiJA9RQ4BLXwuR6RIyCxF/Y9UIx99kQVpYtB0MDY4bNsUYvfPTOYJl+YXCpjDxK3AXSEPfxKqs3j4evp/JaMDmCEIHNNOe2xZ+n2k3Z83IgC19bhSqabB66Yp6lBHlkZJ3hvSiet3h/xaFgf+ecbpkzGXAQebKYvfZ/v6p99wmJX+R/WtosNAmRQWP+aSHjhqQn4yrjWWCi6/ByUYCmxa6mViMVwqCtFdSqXyieRuV6DFgXsJenjVX9JRlHykesiiv5pXqGfulMjLdQz/BUlclsIK9v6Utd50amWNHmeQ35RRYsRYZ4qO92WLhb/6n48dxzo72NgKvWdsuA0RTDR0sK4zIHH7I9mebY8X9+1UEDs3Nsm3y3G2spzyQRWOTy5BUwt9WFQcksFrr8+J4i0FW55QfgWZI7pcJIydIPAXljTtyCOWR7WqRJqx7xGnUDPf64DiQyBOIgVbGmuNbsLQuI9twaCQ6yMqDf27ezieq58zHwOwHKS5r19DBrcFwyoqnh2Fwl0OQLeobLBainI6tOXvEHQmoBVqzE="
                        ).create();
        player.getInventory().addItem(item);
    }

    @Subcommand("inv")
    public void inv(Player player, String modelName){
        Npcs npc = Npcs.Jayden;
        NpcInstance pni = new NpcInstance(plugin, npc.getHandler(), player.getLocation(), Bukkit.getOnlinePlayers().toArray(new Player[0]));
        Entity ent = pni.spawn().getBukkitEntity();

        ActiveModel model = ModelEngineAPI.api.getModelManager().createActiveModel(modelName);
        Entity mob = player.getWorld().spawnEntity(player.getLocation(), EntityType.HUSK);
        ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().createModeledEntity(ent);

        modeledEntity.addActiveModel(model);
        modeledEntity.detectPlayers();
        modeledEntity.setInvisible(true);
    }

    @Subcommand("testboard")
    public void testboard(Player player){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                ScoreboardManager manager = Bukkit.getScoreboardManager();
                final Scoreboard board = manager.getNewScoreboard();
                final Objective objective = board.registerNewObjective("test", "dummy");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName(Util.formatColour("&8Quests"));
                Score score = objective.getScore(Util.formatColour("&f\uF006"));
                score.setScore(10);
                player.setScoreboard(board);

        },0, 20 * 10);
    }

    @Subcommand("debug")
    public class debug extends BaseCommand{

        @Subcommand("clear")
        public class clear extends BaseCommand{

            @Subcommand("armorStand")
            public void armorStand(Player player){
                for(ArmorStand ent : player.getLocation().getWorld().getEntitiesByClass(ArmorStand.class)){
                    ent.remove();
                }
            }
        }

        @Subcommand("health")
        public class health extends BaseCommand{

            @Subcommand("set")
            public void set(Player player, double amt){
                PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
                if(playerData.getActiveProfileID() == null)
                    return;

                ProfileData profileData = plugin.getPlayerManager().getProfile(playerData.getActiveProfileID());
                profileData.setHealth(amt);
            }
        }
    }
}




























