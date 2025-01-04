package com.ferra13671.BThack.api.Events;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.network.packet.Packet;

public class PacketEvent extends Event {
    private Packet<?> packet;

    public PacketEvent(Packet<?> packet) {

        this.packet = packet;
    }

    public final Packet<?> getPacket() {

        return this.packet;
    }


    public final Packet<?> setPacket(Packet<?> packet) {

        return this.packet = packet;
    }

    public static class Send extends PacketEvent {

        public Send(Packet<?> packet) {

            super(packet);
        }
    }

    public static class Receive extends PacketEvent {

        public Receive(Packet<?> packet) {
            super(packet);
        }
    }
}
