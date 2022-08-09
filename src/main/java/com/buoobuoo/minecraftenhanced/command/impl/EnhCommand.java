package com.buoobuoo.minecraftenhanced.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.ability.Ability;
import com.buoobuoo.minecraftenhanced.core.ability.AbilityManager;
import com.buoobuoo.minecraftenhanced.core.entity.EntityManager;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.event.update.PlayerLevelUpEvent;
import com.buoobuoo.minecraftenhanced.core.item.AbilityGemItem;
import com.buoobuoo.minecraftenhanced.core.item.CustomItemManager;
import com.buoobuoo.minecraftenhanced.core.item.CustomItems;
import com.buoobuoo.minecraftenhanced.core.player.PlayerData;
import com.buoobuoo.minecraftenhanced.core.player.ProfileData;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.rework.quest.QuestLine;
import com.buoobuoo.minecraftenhanced.rework.quest.impl.TestQuest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("enh")
public class EnhCommand extends BaseCommand {

    private final MinecraftEnhanced plugin;

    public EnhCommand(MinecraftEnhanced plugin){
        this.plugin = plugin;
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

    @Subcommand("debug")
    public class debug extends BaseCommand{

        @Subcommand("route")
        public void route(Player player){
            plugin.getRouteManager().showRoute(player);
        }

        @Subcommand("test")
        public void test(Player player){
            TestQuest questLine = new TestQuest(plugin);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                questLine.setDeterminant(player, "killed rat", true);
                }, 80);

            plugin.registerEvents(questLine);
            questLine.start(player);
        }

        @Subcommand("give")
        public class give extends BaseCommand{

            @Subcommand("item")
            @CommandCompletion("@custom-items")
            public void item(Player player, CustomItems item){
                ProfileData profileData = plugin.getPlayerManager().getProfile(player);
                ItemStack stack = plugin.getCustomItemManager().getItem(profileData, item);
                stack = item.getHandler().update(plugin, profileData, stack);

                player.getInventory().addItem(stack);
            }

            @Subcommand("item")
            @CommandCompletion("@custom-items")
            public void item(Player player, CustomItems item, int amount){
                ProfileData profileData = plugin.getPlayerManager().getProfile(player);
                ItemStack stack = plugin.getCustomItemManager().getItem(profileData, item);
                stack.setAmount(amount);
                stack = item.getHandler().update(plugin, profileData, stack);

                player.getInventory().addItem(stack);
            }

            @Subcommand("ability")
            @CommandCompletion("@abilities")
            public void ability(Player player, String abilityID){
                ProfileData profileData = plugin.getPlayerManager().getProfile(player);
                AbilityManager abilityManager = plugin.getAbilityManager();
                CustomItemManager customItemManager = plugin.getCustomItemManager();

                Ability ability = abilityManager.getAbilityByID(abilityID);

                AbilityGemItem gemItem = new AbilityGemItem(ability);
                ItemStack item = customItemManager.getItem(profileData, gemItem);
                player.getInventory().addItem(item);
            }
        }

        @Subcommand("spawn")
        @CommandCompletion("@custom-entities")
        public void spawn(Player player, String entityClass){
            EntityManager entityManager = plugin.getEntityManager();
            Class<? extends CustomEntity> clazz = entityManager.getHandlerClassByName(entityClass);
            CustomEntity ent = entityManager.spawnEntity(clazz, player.getLocation());
        }

        @Subcommand("clear")
        public class clear extends BaseCommand{

            @Default
            @CommandCompletion("@vanilla-entity-type")
            public void type(Player player, EntityType type){
                for(Entity ent : player.getLocation().getWorld().getEntitiesByClass(type.getEntityClass())){
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

        @Subcommand("level")
        public class level extends BaseCommand{

            @Subcommand("set")
            public void set(Player player, int amt){
                PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
                if(playerData.getActiveProfileID() == null)
                    return;

                ProfileData profileData = plugin.getPlayerManager().getProfile(playerData.getActiveProfileID());
                profileData.setLevel(amt);

                Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(player));
            }
        }

        @Subcommand("mana")
        public class mana extends BaseCommand{

            @Subcommand("set")
            public void set(Player player, int amt){
                PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
                if(playerData.getActiveProfileID() == null)
                    return;

                ProfileData profileData = plugin.getPlayerManager().getProfile(playerData.getActiveProfileID());
                profileData.setMana(amt);

                Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(player));
            }
        }


        @Subcommand("exp")
        public class exp extends BaseCommand{

            @Subcommand("set")
            public void set(Player player, int amt){
                PlayerData playerData = plugin.getPlayerManager().getPlayer(player);
                if(playerData.getActiveProfileID() == null)
                    return;

                ProfileData profileData = plugin.getPlayerManager().getProfile(playerData.getActiveProfileID());
                profileData.setExperience(amt);

                Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(player));
            }
        }
    }
}




























