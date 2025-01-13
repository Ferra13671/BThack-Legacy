package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.Entity.SetVelocityEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.effect.StatusEffects;

public class LevitationControl extends Module {

    public LevitationControl() {
        super("LevitationControl",
                "lang.module.LevitationControl",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }


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
