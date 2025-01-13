package com.ferra13671.BThack.api.Events.Camera;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.util.math.Vec2f;

public class RotateCameraEvent extends Event {

    private float yaw;
    private float pitch;

    public RotateCameraEvent(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setRotation(Vec2f rotation) {
        yaw = rotation.x;
        pitch = rotation.y;
    }
}
