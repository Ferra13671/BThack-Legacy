package com.ferra13671.BThack.api.Events.Entity;


import com.ferra13671.MegaEvents.Base.Event;

public class JumpHeightEvent extends Event {

    private float jumpHeight;

    public JumpHeightEvent(float jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    public float getJumpHeight() {
        return jumpHeight;
    }

    public void  setJumpHeight(float value) {
        this.jumpHeight = value;
    }
}
