package com.ferra13671.BThack.api.Managers.managers.TravelChange;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Events.Player.PlayerTraverRotEvent;
import com.ferra13671.BThack.api.Events.Player.VelocityUpdateEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.mixins.accessor.packet.IPlayerMoveC2SPacket;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TravelChangeManager implements Initializable, Mc {
    /*
    TravelChangers priority:

    LongJump(Elytra&Firework mode): 1000000
    ElytraFlight: 1000000
    KillAura: 5000
    MoveTask/TunnelTask: 1000
    Sprint: 500
     */

    private final List<TravelChanger> changers = new CopyOnWriteArrayList<>();
    private float yaw;
    private float pitch;

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
        BThack.debug("Travel Change Manager inited!");
    }

    public void addChanger(TravelChanger changer) {
        if (!changers.contains(changer)) {
            changers.add(changer);
            filterChangers();
        }
    }

    public void removeChanger(TravelChanger changer) {
        if (changers.contains(changer)) {
            changers.remove(changer);
            filterChangers();
        }
    }

    public boolean containsChanger(TravelChanger changer) {
        return changers.contains(changer);
    }

    private void filterChangers() {
        changers.sort(Comparator.comparing(changer -> changer.priority));
        Collections.reverse(changers);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (Module.nullCheck()) return;
        if (!changers.isEmpty()) {
            Float[] rots = changers.getFirst().rotateGetter.get();
            yaw = rots[0];
            pitch = rots[1];
        }
    }

    @EventSubscriber
    public void onTravelRot(PlayerTraverRotEvent e) {
        if (!changers.isEmpty()) {
            TravelChanger changer = changers.getFirst();
            if (changer.needRewriteTravelRot && changer.needTravelChange.get()) {
                e.yaw = yaw;
                e.pitch = pitch;
            }
        }
    }

    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (!changers.isEmpty()) {
            if (e.getPacket() instanceof PlayerMoveC2SPacket packet && (packet instanceof PlayerMoveC2SPacket.Full || packet instanceof PlayerMoveC2SPacket.LookAndOnGround)) {
                IPlayerMoveC2SPacket iPacket = (IPlayerMoveC2SPacket) packet;
                if (iPacket._getYaw() == mc.player.getYaw() && iPacket._getPitch() == mc.player.getPitch()) {
                    if (changers.getFirst().needTravelChange.get()) {
                        iPacket.setYaw(yaw);
                        iPacket.setPitch(pitch);
                    }
                }
            }
        }
    }

    @EventSubscriber
    public void onUpdateVelocity(VelocityUpdateEvent e) {
        if (!changers.isEmpty()) {
            if (!mc.player.isFallFlying() && changers.getFirst().needTravelChange.get()) {
                e.setVelocity(AimBotUtils.movementInputToVelocity(e.getMovementInput(), e.getSpeed(), yaw));
                changers.getFirst().preUpdateVelocityRunnable.run();
            }
        }
    }
}
