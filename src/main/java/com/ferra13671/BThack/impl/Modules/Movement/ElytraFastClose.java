package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.mixins.accessor.entity.IEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "ElytraFastClose", description = "lang.module.ElytraFastClose", category = "MOVEMENT")
public class ElytraFastClose extends Module {

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.verticalCollision) {
            if (ModuleList.elytraFlight.isEnabled()) return;
            IEntity entity = (IEntity) mc.player;
            if (entity.invokeGetFlag(7))
                entity.invokeSetFlag(7, false);
        }
    }
}
