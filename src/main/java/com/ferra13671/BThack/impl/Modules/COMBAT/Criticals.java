package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.ArrayList;
import java.util.Arrays;

public class Criticals extends Module {

    public static ModeSetting mode;

    public Criticals() {
        super("Criticals",
                "lang.module.Criticals",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Packet", "Bypass")));

        initSettings(
                mode
        );
    }

    @EventSubscriber
    public void onUpdate(AttackEntityEvent e) {
        if (e.getEntity() instanceof EndCrystalEntity || e.getPlayer() != mc.player) return;

        if (!mc.player.onGround || PlayerUtils.isInWater() || mc.player.isInLava()) return;

        e.setCancelled(true);

        switch (mode.getValue()) {
            case "Packet":
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.1f, mc.player.getZ(), false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));

                mc.player.fallDistance = 1;
                mc.player.onGround = false;

                mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(e.getEntity(), mc.player.isSneaking()));
                mc.player.attack(e.getEntity());
                mc.player.resetLastAttackedTicks();

                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(mc.player.onGround));
                mc.player.addCritParticles(e.getEntity());
                break;
            case "Bypass":
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.1625, mc.player.getZ(), false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 4.0E-6, mc.player.getZ(), false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.0E-6, mc.player.getZ(), false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));

                mc.player.fallDistance = 1;
                mc.player.onGround = false;

                mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(e.getEntity(), mc.player.isSneaking()));
                mc.player.attack(e.getEntity());
                mc.player.resetLastAttackedTicks();

                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(mc.player.onGround));
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
