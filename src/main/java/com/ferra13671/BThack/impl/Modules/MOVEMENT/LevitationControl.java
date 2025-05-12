package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.Entity.SetVelocityEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.effect.StatusEffects;

@ModuleInfo(name = "LevitationControl", description = "lang.module.LevitationControl", category = "MOVEMENT")
public class LevitationControl extends Module {

    @EventSubscriber
    public void onMove(SetVelocityEvent e) {
        if (mc.player.hasStatusEffect(StatusEffects.LEVITATION)) {
            double yMove = e.getVelocity().y;
            if (!mc.options.jumpKey.isPressed()) {
                yMove = 0;
            }

            e.getVelocity().y = yMove;
        }
    }
}
