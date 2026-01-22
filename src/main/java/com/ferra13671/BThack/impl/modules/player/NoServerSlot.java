package com.ferra13671.BThack.impl.modules.player;

import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;

@ModuleInfo(name = "NoServerSlot", description = "lang.module.NoServerSlot", category = "PLAYER")
public class NoServerSlot extends Module {


    @EventSubscriber
    @SuppressWarnings("DataFlowIssue")
    public void onPacketReceive(PacketEvent.Receive event) {
        if (nullCheck()) return;
        if (event.getPacket() instanceof UpdateSelectedSlotS2CPacket) {
            event.cancel();
            Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
        }
    }
}
