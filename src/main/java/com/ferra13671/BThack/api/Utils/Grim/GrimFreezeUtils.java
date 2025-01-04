package com.ferra13671.BThack.api.Utils.Grim;

import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;

import java.util.ArrayList;
import java.util.List;

public final class GrimFreezeUtils implements Mc {

    private static final List<CommonPongC2SPacket> packetList = new ArrayList<>();
    private static long freezeMillis = 0;

    private static final ArrayList<Packet<?>> packets = new ArrayList<>();


    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (freezeMillis <= 0) {
            return;
        }
        if (e.getPacket() instanceof CommonPongC2SPacket packet) {
            e.setCancelled(true);
            packetList.add(packet);
        }
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (freezeMillis <= 0) {
            return;
        }
        freezeMillis -= 50;
        if (freezeMillis <= 0) {
            if (!packetList.isEmpty()) {
                for (CommonPongC2SPacket p : packetList) {
                    mc.getNetworkHandler().sendPacket(p);
                }
                packetList.clear();
                for (Packet<?> packet : packets) {
                    mc.getNetworkHandler().sendPacket(packet);
                }
            }
        }
    }

    public static void freeze(long millis) {
        freezeMillis = millis;
    }

    public static void stopFreeze() {
        freezeMillis = 0;
    }

    public static long getFreezeMillis() {
        return freezeMillis;
    }

    public static boolean isFreezing() {
        return freezeMillis > 0;
    }

    public static void setPostPackets(List<Packet<?>> newPackets) {
        packets.addAll(newPackets);
    }
}
