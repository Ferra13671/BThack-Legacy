package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.ArrayList;
import java.util.Arrays;

@ModuleInfo(name = "Criticals", description = "lang.module.Criticals", category = "COMBAT")
public class Criticals extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Packet", "Bypass")));


    @EventSubscriber
    public void onUpdate(AttackEntityEvent e) {
        if (e.getEntity() instanceof EndCrystalEntity || e.getPlayer() != mc.player) return;

        if (!mc.player.onGround || PlayerUtils.isInWater() || mc.player.isInLava()) return;

        e.setCancelled(true);

        switch (mode.getValue()) {
            case "Packet":
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.1f, mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, mc.player.horizontalCollision));

                mc.player.fallDistance = 1;
                mc.player.onGround = false;

                Managers.NETWORK_MANAGER.sendPacket(PlayerInteractEntityC2SPacket.attack(e.getEntity(), mc.player.isSneaking()));
                mc.player.attack(e.getEntity());
                mc.player.resetLastAttackedTicks();

                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(mc.player.onGround, mc.player.horizontalCollision));
                mc.player.addCritParticles(e.getEntity());
                break;
            case "Bypass":
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.1625, mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 4.0E-6, mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.0E-6, mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, mc.player.horizontalCollision));

                mc.player.fallDistance = 1;
                mc.player.onGround = false;

                Managers.NETWORK_MANAGER.sendPacket(PlayerInteractEntityC2SPacket.attack(e.getEntity(), mc.player.isSneaking()));
                mc.player.attack(e.getEntity());
                mc.player.resetLastAttackedTicks();

                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(mc.player.onGround, mc.player.horizontalCollision));
                mc.player.addCritParticles(e.getEntity());
                break;
                /*
            case "MiniJump":
                mc.player.jump();
                mc.player.velocity.y = 0.25;
                mc.player.fallDistance = 1;
                mc.player.onGround = false;
                break;
            case "Jump":
                mc.player.jump();
                mc.player.fallDistance = 1;
                mc.player.onGround = false;
                break;

                 */
        }
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = mode.getValue();
    }
}
