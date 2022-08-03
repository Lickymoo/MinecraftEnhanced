package com.buoobuoo.minecraftenhanced.core.entity.npc;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.entity.npc.pathfinder.PlayerMoveControl;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

@Getter
public class NpcInstance {
    private final MinecraftEnhanced plugin;
    private UUID uuid;
    private int id;
    private GameProfile gameProfile;

    private Npc npcHandler;
    private NpcEntity entity;

    private Player[] players;

    private PlayerMoveControl moveController;

    public NpcInstance(MinecraftEnhanced plugin, Npc handler, World world, Player... players){
        this.plugin = plugin;
        //create Entity
        this.npcHandler = handler;
        this.uuid = UUID.randomUUID();
        this.id = Util.randomInt(0, Integer.MAX_VALUE);

        this.players = players;

        generate(world);
        this.moveController = new PlayerMoveControl(this);

        plugin.getNpcManager().registerNpcInstance(this);
    }


    public NpcInstance(MinecraftEnhanced plugin, Npc handler, Location loc, Player... players) {
        this.plugin = plugin;
        //create Entity
        this.npcHandler = handler;
        this.uuid = UUID.randomUUID();
        this.id = Util.randomInt(0, Integer.MAX_VALUE);

        this.players = players;

        generate(loc.getWorld());
        this.moveController = new PlayerMoveControl(this);
        setLocation(loc);
        setSkin(npcHandler.getSkinSignature(), npcHandler.getSkinTexture());

        plugin.getNpcManager().registerNpcInstance(this);
    }

    public void generate(World world){
        CraftWorld cw = (CraftWorld)world;
        ServerLevel level = cw.getHandle();
        MinecraftServer server = level.getServer();

        gameProfile = new GameProfile(uuid, npcHandler.getDisplayName());
        entity = new NpcEntity(server, level, gameProfile);
    }

    public void setLocation(Location loc){
        entity.setPos(loc.getX(), loc.getY(), loc.getZ());
    }

    public void setSkin(String signature, String texture){
        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));
    }

    public Entity spawn(){
        npcHandler.onSpawn(entity, getPlayers());
        Util.sendPacket(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, entity), getPlayers());
        Util.sendPacket(new ClientboundAddPlayerPacket(entity), getPlayers());
        Util.sendPacket(new ClientboundSetEntityDataPacket(entity.getId(), entity.getEntityData(), true), getPlayers());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->{
            this.moveController.tick();
            }, 1, 1);

        setSkin("Eg2qotZYEF6DVpiGjr2gz76PIsPPLNjDsvbQi4ITgi+SSeuq5FqZ6yu9Eshk58OgkjThAPw9fpwOAech+u2RTxs2iILU5k35ZQZafq6wAVmBO2iII1V1tdhpYldhsQrT7DXWneptElU2PuPclKZtGAz60RcES0ZIdpbbMFaZGhNRO8q18tNb8Qa4UYGq7H0yqn9xYDaJMwtXJObLfBq0bIKKO1tyVRW6GRCUWkC8nrm75jfWWNbEte5PPyEpPQg+Uor/o0ZdbNbstnG3joLuoIAH+fiVRb/mV/jYzniwHRKD8zfnpBwsii6oaxdq4zeAupldcbaBPFscqIoUe80M9/m6fBJi5+WJKP7A/Pv/plHJu+7HYDGeDxE817Q8ajx6ZTB+qgmf+ERxca5PmLyj2Zz8HQ7U6ZSEIY5rIedtahwImvvbShVUrnWfZzXkxFCFmXCoWD+0rZ9MLWPqXtaTMLIETq92H+Eve6nGxG2JZONGoW3Pq+9gINLsnz6Y9gMXZLPiY9iEQo0JREVi+8g52V/lVx76l0WqSKXZXts8p1YF05LHDaC4a99qu5Qnh3UpKIoRXjfct5qs60UGGNhk+l6cIDYmtceSKvir3SL39h96u21f6mcSH1b9SDJekNk2fOeSCDm1s9IYLM67qQCrd4EBMvGiC24cJVlrQki6xQI=",
                "ewogICJ0aW1lc3RhbXAiIDogMTY1OTA1NzEyMzk2MywKICAicHJvZmlsZUlkIiA6ICIyM2YxYTU5ZjQ2OWI0M2RkYmRiNTM3YmZlYzEwNDcxZiIsCiAgInByb2ZpbGVOYW1lIiA6ICIyODA3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRhZTNmNzJlZDYwMTgzMjZlMTAwYzk0ODcxNzg3ODVhOTA1NGQ3YjgxNDEzOTEzNjVkOGFiNTdhMjUxNDQyNWQiCiAgICB9CiAgfQp9");
        return entity;
    }

    public void destroy(){
        npcHandler.onDestroy(entity, getPlayers());
        Util.sendPacket(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, entity), getPlayers());
        Util.sendPacket(new ClientboundRemoveEntitiesPacket(entity.getId()), getPlayers());
        entity.remove(Entity.RemovalReason.DISCARDED);
    }

    public boolean isActive(){
        for(Player player : players){
            if(player.isOnline())
                return true;
        }
        players = null;
        entity.remove(Entity.RemovalReason.DISCARDED);
        return false;
    }


    //MOVE LATER
    public void moveEntityTo(double x, double y, double z) {
        double blocksPerTick = .10;
        Vector vector = new Vector(x, y, z);
        Vector vector2 = new Vector(entity.getX(), entity.getY(), entity.getZ());
        Vector difference = vector.clone().subtract(vector2);
        double distance =  vector2.distance(vector);
        if (distance <= 0) {
            Bukkit.broadcastMessage("Not moving! Distance is 0!");
            return;
        } else if (distance > 8) throw new IllegalArgumentException("Overall distance may not be over 8!");

        ClientboundMoveEntityPacket posPacket = new ClientboundMoveEntityPacket.Pos(
                entity.getId(),
                (short)(blocksPerTick * (difference.getX() / distance) * 4096),
                (short)(blocksPerTick * (difference.getY() / distance) * 4096),
                (short)(blocksPerTick * (difference.getZ() / distance) * 4096),
                true);

        int iterations = (int) Math.round(distance / blocksPerTick);

        for (int xx = 0; xx <= iterations + 1; xx++ ) {
            if (xx == iterations) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Util.sendPacket(posPacket, players);
                    entity.setPos(x, y, z);
                }, xx);
                return;
            }
            final int currentX = xx;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Util.sendPacket(posPacket, players);
            }, xx);
        }
    }
}
