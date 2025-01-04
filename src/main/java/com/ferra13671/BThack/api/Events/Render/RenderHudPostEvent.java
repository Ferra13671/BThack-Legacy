package com.ferra13671.BThack.api.Events.Render;

import com.ferra13671.MegaEvents.Base.Event;

public class RenderHudPostEvent extends Event {
    private final float partialTicks;

    public RenderHudPostEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
