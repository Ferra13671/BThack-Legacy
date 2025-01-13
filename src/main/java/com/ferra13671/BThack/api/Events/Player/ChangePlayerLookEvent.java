package com.ferra13671.BThack.api.Events.Player;


import com.ferra13671.MegaEvents.Base.Event;

public class ChangePlayerLookEvent extends Event {

    public final double cursorDeltaX;
    public final double cursorDeltaY;

    public ChangePlayerLookEvent(double cursorDeltaX, double cursorDeltaY) {
        this.cursorDeltaX = cursorDeltaX;
        this.cursorDeltaY = cursorDeltaY;
    }
}
