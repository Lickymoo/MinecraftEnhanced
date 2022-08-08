package com.buoobuoo.minecraftenhanced.core.event.listener.mechanic;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.interf.CustomEntity;
import com.buoobuoo.minecraftenhanced.core.entity.interf.NpcEntity;
import com.buoobuoo.minecraftenhanced.core.event.update.UpdateSecondEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NpcTeamTagListener implements Listener {

    private final MinecraftEnhanced plugin;

    public NpcTeamTagListener(MinecraftEnhanced plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onUpdate(UpdateSecondEvent event){
        for(CustomEntity entity : plugin.getEntityManager().getRegisteredEntities()){
            if(!(entity instanceof NpcEntity))
                return;

            Player player = (Player)entity.asEntity().getBukkitEntity();

            Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
            Team team = scoreboard.getTeam(player.getName());

            if(team == null)
                team = scoreboard.registerNewTeam(player.getName());
            team.addEntry(player.getName());
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
    }
}
