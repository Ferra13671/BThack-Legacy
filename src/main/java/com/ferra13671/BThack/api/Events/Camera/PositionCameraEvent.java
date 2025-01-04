package com.ferra13671.BThack.api.Events.Camera;


import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.util.math.Vec3d;

public class PositionCameraEvent extends Event {

    private double x;
    private double y;
    private double z;
    private final float tickDelta;

    public PositionCameraEvent(double x, double y, double z, float tickDelta) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tickDelta = tickDelta;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public void setPosition(Vec3d pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }
}
