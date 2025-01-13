package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class ElytraFastClose extends Module {

    public ElytraFastClose() {
        super("ElytraFastClose",
                "lang.module.ElytraFastClose",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }

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
