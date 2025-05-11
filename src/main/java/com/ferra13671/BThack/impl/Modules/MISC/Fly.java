package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.Entity.SetVelocityEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.BThack.mixins.accessor.packet.IPlayerMoveC2SPacket;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Fly extends Module {

    public final NumberSetting speed = new NumberSetting("Speed", this, 0.5, 0.1, 2.0, false);

    public Fly() {
        super("Fly",
                "lang.module.Fly",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }

    @EventSubscriber
    public void onTick(SetVelocityEvent e) {
        Vec3d newVec3d = e.getVelocity();

        if (isMoving()) {
            double yaw = Math.toRadians(StrafeUtils.getPlayerYawOnKeybindings());
            newVec3d.x = -Math.sin(yaw) * speed.getValue();
            newVec3d.z = Math.cos(yaw) * speed.getValue();
        } else {
            newVec3d.x = 0;
            newVec3d.z = 0;
        }

        if (mc.options.jumpKey.isPressed()) {
            newVec3d.y = speed.getValue();
        } else
        if (mc.options.sneakKey.isPressed()) {
            newVec3d.y = -speed.getValue();
        } else
            newVec3d.y = 0;

        e.setVelocity(newVec3d);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onPacket(PacketEvent.Send e) {
        if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            ((IPlayerMoveC2SPacket) packet).setOnGround(true);
        }
    }

    private boolean isMoving() {
        return mc.options.forwardKey.isPressed() || mc.options.backKey.isPressed() || mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed();
    }
}
