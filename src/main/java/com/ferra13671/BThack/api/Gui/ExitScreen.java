package com.ferra13671.BThack.api.Gui;

import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.Ticker;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ExitScreen extends BThackScreen {
    private final Ticker delayTicker = new Ticker();

    public ExitScreen() {
        super(Text.of("Exit"));
    }

    @Override
    public void onDisplayed() {
        delayTicker.reset();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        if (delayTicker.passed(500)) mc.stop();
    }
}
