package com.ferra13671.BThack.impl.modules.combat;

import com.ferra13671.BThack.events.entity.AttackEntityEvent;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.PlayerUtils;
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
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onUpdate(AttackEntityEvent e) {
        if (e.getEntity() instanceof EndCrystalEntity || e.getPlayer() != mc.player) return;

        if (!mc.player.onGround || PlayerUtils.isInWater() || mc.player.isInLava()) return;

        e.setCancelled(true);

        switch (mode.getValue()) {
            case "Packet" -> {
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.1f, mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, mc.player.horizontalCollision));

                mc.player.onGround = false;

                Managers.NETWORK_MANAGER.sendPacket(PlayerInteractEntityC2SPacket.attack(e.getEntity(), mc.player.isSneaking()));
                mc.player.attack(e.getEntity());
                mc.player.resetLastAttackedTicks();

                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(mc.player.onGround, mc.player.horizontalCollision));
                mc.player.addCritParticles(e.getEntity());
            }
            case "Bypass" -> {
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.1625, mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 4.0E-6, mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.0E-6, mc.player.getZ(), false, mc.player.horizontalCollision));
                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, mc.player.horizontalCollision));

                mc.player.onGround = false;

                Managers.NETWORK_MANAGER.sendPacket(PlayerInteractEntityC2SPacket.attack(e.getEntity(), mc.player.isSneaking()));
                mc.player.attack(e.getEntity());
                mc.player.resetLastAttackedTicks();

                Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(mc.player.onGround, mc.player.horizontalCollision));
                mc.player.addCritParticles(e.getEntity());
            }
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = mode.getValue();
    }
}
