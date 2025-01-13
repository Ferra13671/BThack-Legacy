package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;

//I don't want to do this
@Deprecated
public class InstaRemine extends Module {

    public InstaRemine() {
        super("InstaRemine",
                "",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }

    private BlockPos blockPos;

    @EventSubscriber
    public void onSend (PacketEvent.Send e){
        if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK)
                blockPos = packet.getPos();
            if (packet.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK && blockPos != null) {
                if (packet.getPos().equals(blockPos))
                    Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
            }
        }
    }
}