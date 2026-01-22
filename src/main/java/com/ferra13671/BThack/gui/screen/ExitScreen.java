package com.ferra13671.BThack.gui.screen;

import com.ferra13671.BThack.core.client.systems.gui.Screen.BThackScreen;
import com.ferra13671.BThack.api.utils.Ticker;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ExitScreen extends BThackScreen {
    private final Ticker delayTicker = new Ticker();

    public ExitScreen() {
        super(Text.literal("Exit"));
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
