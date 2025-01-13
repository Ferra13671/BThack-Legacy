package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class XCarry extends Module {

    public XCarry() {
        super("XCarry",
                "lang.module.XCarry",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }

    @EventSubscriber
    public void onSendPacket(PacketEvent.Send e) {
        if (e.getPacket() instanceof CloseHandledScreenC2SPacket packet
                && (packet.getSyncId() == mc.player.playerScreenHandler.syncId
                )) {
            e.setCancelled(true);
        }
    }
}
