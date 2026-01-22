package com.ferra13671.BThack.impl.modules.movement;

import com.ferra13671.BThack.events.entity.SetVelocityEvent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.effect.StatusEffects;

@ModuleInfo(name = "LevitationControl", description = "lang.module.LevitationControl", category = "MOVEMENT")
public class LevitationControl extends Module {

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onMove(SetVelocityEvent e) {
        if (mc.player.hasStatusEffect(StatusEffects.LEVITATION)) {
            double yMove = e.getVelocity().y;
            if (!mc.options.jumpKey.isPressed())
                yMove = 0;

            e.getVelocity().y = yMove;
        }
    }
}
