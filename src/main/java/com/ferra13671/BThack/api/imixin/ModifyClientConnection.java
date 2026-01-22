package com.ferra13671.BThack.api.imixin;

import net.minecraft.network.packet.Packet;

public interface ModifyClientConnection {

    void sendPacketNoEvent(Packet<?> packet);
}
