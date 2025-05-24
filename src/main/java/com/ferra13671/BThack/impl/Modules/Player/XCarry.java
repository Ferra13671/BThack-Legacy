package com.ferra13671.BThack.impl.Modules.Player;

import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

@ModuleInfo(name = "XCarry", description = "lang.module.XCarry", category = "PLAYER")
public class XCarry extends Module {

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onSendPacket(PacketEvent.Send e) {
        if (e.getPacket() instanceof CloseHandledScreenC2SPacket packet
                && (packet.getSyncId() == mc.player.playerScreenHandler.syncId
                )) {
            e.setCancelled(true);
        }
    }
}
