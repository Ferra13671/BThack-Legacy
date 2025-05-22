package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.mixins.accessor.packet.IEntityVelocityUpdateS2CPacket;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@ModuleInfo(name = "Velocity", description = "lang.module.Velocity", category = "MOVEMENT")
public class Velocity extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Grim" ,"Normal", "Cancel")));

    public final BooleanSetting liquid = new BooleanSetting("Liquid", this, false);
    public final BooleanSetting fallFlying = new BooleanSetting("FallFlying", this, false);

    public final BooleanSetting velocity = new BooleanSetting("Velocity", this, true);
    public final NumberSetting velocityV = new NumberSetting("Velocity Vertical", this, 0.0,0.0,100.0,true, () -> velocity.getValue() && mode.getValue().equals("Normal"));
    public final NumberSetting velocityH = new NumberSetting("Velocity Horizontal", this, 0.0,0.0,100.0,true, () -> velocity.getValue() && mode.getValue().equals("Normal"));

    public final BooleanSetting explosion = new BooleanSetting("Explosions", this, true);
    public final NumberSetting explosionV = new NumberSetting("Expl Vertical", this, 0.0,0.0,100.0,true, () -> explosion.getValue() && mode.getValue().equals("Normal"));
    public final NumberSetting explosionH = new NumberSetting("Expl Horizontal", this, 0.0,0.0,100.0,true, () -> explosion.getValue() && mode.getValue().equals("Normal"));


    private boolean flag;
    private int ticks;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        arrayListInfo = mode.getValue();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        arrayListInfo = mode.getValue();
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck()) return;
        if (!fallFlying.getValue() && mc.player.isGliding()) return;

        if ((mc.player.isTouchingWater() || mc.player.isSubmergedInWater() || mc.player.isInLava()) && !liquid.getValue())
            return;

        double velV = velocityV.getValue();
        double velH = velocityH.getValue();
        float explV = explosionV.getValue().floatValue();
        float explH = explosionH.getValue().floatValue();

        if (ticks > 0) {
            ticks--;
            return;
        }
        if (e.getPacket() instanceof PlayerPositionLookS2CPacket) {
            if (mode.getValue().equals("Grim"))
                ticks = 5;
        }

        if (e.getPacket() instanceof EntityVelocityUpdateS2CPacket packet && velocity.getValue()) {
            if (packet.getEntityId() == mc.player.getId()) {
                switch (mode.getValue()) {
                    case "Normal" -> {
                        velV /= 100;
                        velH /= 100;
                        IEntityVelocityUpdateS2CPacket iPacket = (IEntityVelocityUpdateS2CPacket) packet;
                        iPacket.setVelocityX((int) (iPacket._getVelocityX() * velH));
                        iPacket.setVelocityY((int) (iPacket._getVelocityY() * velV));
                        iPacket.setVelocityZ((int) (iPacket._getVelocityZ() * velH));
                    }
                    case "Cancel" -> e.setCancelled(true);
                    case "Grim" -> {
                        e.setCancelled(true);
                        flag = true;
                    }
                }
            }
        }
        if (e.getPacket() instanceof ExplosionS2CPacket packet && explosion.getValue()) {
            Vec3d playerKnockback = packet.playerKnockback().orElse(null);
            if (playerKnockback == null) return;
            switch (mode.getValue()) {
                case "Normal" -> {
                    explV = explV / 100;
                    explH = explH / 100;
                    playerKnockback.x *= explH;
                    playerKnockback.y *= explV;
                    playerKnockback.z *= explH;
                }
                case "Cancel" -> e.setCancelled(true);
                case "Grim" -> {
                    playerKnockback = Vec3d.ZERO;
                    flag = true;
                }
            }
            e.setPacket(new ExplosionS2CPacket(packet.center(), Optional.of(playerKnockback), packet.explosionParticle(), packet.explosionSound()));
        }
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || !mode.getValue().equals("Grim")) return;

        if (!fallFlying.getValue() && mc.player.isGliding()) return;

        if ((mc.player.isTouchingWater() || mc.player.isSubmergedInWater() || mc.player.isInLava()) && !liquid.getValue())
            return;

        if (flag) {
            if (ticks <= 0) {
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.lastYaw, mc.player.lastPitch, mc.player.isOnGround(), mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, BlockPos.ofFloored(mc.player.getPos()), Direction.DOWN));
            }
            flag = false;
        }
    }
}
