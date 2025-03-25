package com.ferra13671.BThack.api.Events.Player;

import com.ferra13671.MegaEvents.Base.Event;

public class SetPlayerYawEvent extends Event {
    private float yaw;

    public SetPlayerYawEvent(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
