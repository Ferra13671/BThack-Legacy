package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;

public abstract class AbstractOneTextComponent extends HudComponent {

    public AbstractOneTextComponent(String name, float x, float y, boolean autoToggled) {
        super(name, x, y, autoToggled);

        height = mc.textRenderer.fontHeight;
    }

    String text = "";

    @Override
    public void render() {
        drawText(text, (int) getX(), (int) getY());
    }

    @Override
    public void tick() {
        text = getText();
        width = mc.textRenderer.getWidth(text);
    }

    public abstract String getText();
}
