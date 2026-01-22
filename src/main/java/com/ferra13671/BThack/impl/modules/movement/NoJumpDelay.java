package com.ferra13671.BThack.impl.modules.movement;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.mixins.accessor.entity.ILivingEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "NoJumpDelay", description = "lang.module.NoJumpDelay", category = "MOVEMENT")
public class NoJumpDelay extends Module {

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onUpdate(ClientTickEvent e) {
        if (nullCheck()) return;

        ((ILivingEntity) mc.player).setJumpingCooldown(0);
    }
}