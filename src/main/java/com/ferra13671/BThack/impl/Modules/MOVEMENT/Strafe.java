package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.Vec3d;

public class Strafe extends Module {

    public Strafe() {
        super("Strafe",
                "lang.module.Strafe",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player != null) {
            if (!mc.player.isFallFlying()) {
                Vec3d velocity = mc.player.getVelocity();

                double currentPlayerSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
                double[] strafeMovements = StrafeUtils.getMoveFactors(currentPlayerSpeed);
                mc.player.velocity.x = strafeMovements[0];
                mc.player.velocity.z = strafeMovements[1];
            }
        }
    }
}
