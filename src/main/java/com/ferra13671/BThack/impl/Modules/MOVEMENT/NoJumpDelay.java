package com.ferra13671.BThack.impl.Modules.MOVEMENT;


import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.entity.ILivingEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class NoJumpDelay extends Module {

    public NoJumpDelay() {
        super("NoJumpDelay",
                "lang.module.NoJumpDelay",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

    }

    @EventSubscriber
    public void onUpdate(ClientTickEvent e) {
        if (nullCheck()) return;

        ((ILivingEntity) mc.player).setJumpingCooldown(0);
    }
}