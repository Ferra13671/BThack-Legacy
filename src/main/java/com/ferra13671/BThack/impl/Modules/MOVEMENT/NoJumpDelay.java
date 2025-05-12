package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.mixins.accessor.entity.ILivingEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "NoJumpDelay", description = "lang.module.NoJumpDelay", category = "MOVEMENT")
public class NoJumpDelay extends Module {

    @EventSubscriber
    public void onUpdate(ClientTickEvent e) {
        if (nullCheck()) return;

        ((ILivingEntity) mc.player).setJumpingCooldown(0);
    }
}