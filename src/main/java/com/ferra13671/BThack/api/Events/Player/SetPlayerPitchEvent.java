package com.ferra13671.BThack.api.Events.Player;

import com.ferra13671.MegaEvents.Base.Event;

public class SetPlayerPitchEvent extends Event {
    private float pitch;

    public SetPlayerPitchEvent(float pitch) {
        this.pitch = pitch;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
