package com.ferra13671.BThack.api.Events;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.client.gui.screen.Screen;

public class GuiOpenEvent extends Event {

    private Screen screen;

    public GuiOpenEvent(Screen screen) {
        this.screen = screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return this.screen;
    }
}
