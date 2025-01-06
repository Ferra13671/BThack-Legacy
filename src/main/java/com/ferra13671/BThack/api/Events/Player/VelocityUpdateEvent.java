package com.ferra13671.BThack.api.Events.Player;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.util.math.Vec3d;

public class VelocityUpdateEvent extends Event {

    private final Vec3d movementInput;
    private final float speed;
    private Vec3d velocity;

    public VelocityUpdateEvent(Vec3d movementInput, float speed, Vec3d velocity) {
        this.movementInput = movementInput;
        this.speed = speed;
        this.velocity = velocity;
    }

    public Vec3d getMovementInput() {
        return this.movementInput;
    }

    public float getSpeed() {
        return this.speed;
    }

    public Vec3d getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vec3d velocity) {
        cancel();
        this.velocity = velocity;
    }
}
