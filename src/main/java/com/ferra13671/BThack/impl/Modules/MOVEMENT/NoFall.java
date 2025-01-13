package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall",
                "lang.module.NoFall",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }


    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            packet.onGround = true;
        }
    }
}
