package com.ferra13671.BThack.managers.impl;

import com.ferra13671.BThack.api.imixin.ModifyClientConnection;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.api.utils.Initializable;
import com.ferra13671.BThack.mixins.accessor.IClientWorld;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.packet.Packet;

public class NetworkManager implements Initializable, Mc {

    public void sendSequencePacket(SequencedPacketCreator packetCreator) {
        if (mc.getNetworkHandler() == null || mc.world == null) return;
        try (PendingUpdateManager pendingUpdateManager = ((IClientWorld) mc.world).getPendingManager().incrementSequence()){
            int i = pendingUpdateManager.getSequence();
            mc.getNetworkHandler().sendPacket(packetCreator.predict(i));
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void sendPacket(Packet<?> packet) {
        mc.getNetworkHandler().sendPacket(packet);
    }

    @SuppressWarnings("DataFlowIssue")
    public void sendPacketNoEvent(Packet<?> packet) {
        ((ModifyClientConnection) mc.getNetworkHandler().getConnection()).sendPacketNoEvent(packet);
    }

    @Override
    public void init() {}
}
