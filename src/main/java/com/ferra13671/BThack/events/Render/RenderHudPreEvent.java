package com.ferra13671.BThack.events.Render;

import com.ferra13671.MegaEvents.Base.Event;

public class RenderHudPreEvent extends Event {
    private final float partialTicks;

    public RenderHudPreEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
