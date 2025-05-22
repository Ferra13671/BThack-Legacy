package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "Strafe", description = "lang.module.Strafe", category = "MOVEMENT")
public class Strafe extends Module {


    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player != null) {
            if (!mc.player.isGliding()) {
                Vec3d velocity = mc.player.getVelocity();

                double currentPlayerSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
                double[] strafeMovements = StrafeUtils.getMoveFactors(currentPlayerSpeed);
                mc.player.velocity.x = strafeMovements[0];
                mc.player.velocity.z = strafeMovements[1];
            }
        }
    }
}
