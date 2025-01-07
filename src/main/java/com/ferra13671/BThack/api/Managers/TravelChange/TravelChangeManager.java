package com.ferra13671.BThack.api.Managers.TravelChange;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Events.Player.PlayerTraverRotEvent;
import com.ferra13671.BThack.api.Events.Player.VelocityUpdateEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TravelChangeManager implements Mc {
    /*
    TravelChangers priority:

    ElytraFlight: 1000000
    KillAura: 5000
    MoveTask/TunnelTask: 1000
    Sprint: 500
     */

    private final List<TravelChanger> changers = new CopyOnWriteArrayList<>();
    private float yaw;
    private float pitch;

    public void addChanger(TravelChanger changer) {
        if (!ModuleList.elytraFlight.mode.getValue().equals("bounce") && !ModuleList.elytraFlight.isEnabled()) {
            return;
        }
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
            Float[] rots = changers.get(0).rotateGetter.get();
            yaw = rots[0];
            pitch = rots[1];
        }
    }

    @EventSubscriber
    public void onTravelRot(PlayerTraverRotEvent e) {
        if (!changers.isEmpty()) {
            TravelChanger changer = changers.get(0);
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
                if (packet.yaw == mc.player.getYaw() && packet.pitch == mc.player.getPitch()) {
                    if (changers.get(0).needTravelChange.get()) {
                        packet.yaw = yaw;
                        packet.pitch = pitch;
                    }
                }
            }
        }
    }

    @EventSubscriber
    public void onUpdateVelocity(VelocityUpdateEvent e) {
        if (!changers.isEmpty()) {
            if (!mc.player.isFallFlying() && changers.get(0).needTravelChange.get()) {
                e.setVelocity(AimBotUtils.movementInputToVelocity(yaw, e.getMovementInput(), e.getSpeed()));
                changers.get(0).preUpdateVelocityRunnable.run();
            }
        }
    }
}
