package com.buoobuoo.minecraftenhanced.core.npc.pathfinder;


import com.buoobuoo.minecraftenhanced.core.npc.NpcInstance;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

@Getter
public class PlayerMoveControl {
    protected NpcInstance instance;
    protected LivingEntity entity;
    private int jumpTicks;
    protected boolean moving;
    protected double speedMod;
    protected double tx;
    protected double ty;
    protected double tz;

    public PlayerMoveControl(NpcInstance instance) {
        this.instance = instance;
        this.entity = instance.getEntity();
        this.tx = entity.getX();
        this.ty = entity.getY();
        this.tz = entity.getZ();
    }

    protected int jumpTicks() {
        return new Random().nextInt(20) + 10;
    }

    protected float rotlerp(float f, float f1, float f2) {
        float f3 = Mth.wrapDegrees(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        float f4 = f + f3;

        if (f4 < 0.0F)
            f4 += 360.0F;
        else if (f4 > 360.0F) {
            f4 -= 360.0F;
        }

        return f4;
    }

    public void setWantedPosition(double x, double y, double z, double speedMod) {
        this.tx = x;
        this.ty = y;
        this.tz = z;
        this.speedMod = speedMod;
        this.moving = true;
    }

    public void setWantedPosition(Location loc, double speedMod) {
        this.tx = loc.getX();
        this.ty = loc.getY();
        this.tz = loc.getZ();
        this.speedMod = speedMod;
        this.moving = true;
    }


    private boolean shouldJump() {
        if (this.jumpTicks-- <= 0) {
            return true;
        }
        return false;
    }

    public void tick() {
        //look at player
        for(Player player : getInstance().getPlayers()){
            Location loc = entity.getBukkitEntity().getLocation();
            loc.setDirection(player.getLocation().subtract(loc).toVector());
            float yaw = loc.getYaw();
            float pitch = loc.getPitch();

            Util.sendPacket(new ClientboundRotateHeadPacket(entity, (byte) ((yaw%360)*256/360)), player);
            Util.sendPacket(new ClientboundMoveEntityPacket.Rot(entity.getId(), (byte)((yaw%360)*256/360), (byte)((pitch%360)*256/360), false), player);
            //Util.sendPacket(new ClientboundMoveEntityPacket.Pos(entity.getId(), (short)1, (short)1, (short)1, false), player);
        }
    }
}
