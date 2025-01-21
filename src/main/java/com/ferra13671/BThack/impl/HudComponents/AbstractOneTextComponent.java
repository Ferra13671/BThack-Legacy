package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.HudComponent.HudComponent;

public abstract class AbstractOneTextComponent extends HudComponent {

    public AbstractOneTextComponent(String name, float x, float y, boolean autoToggled) {
        super(name, x, y, autoToggled);
    }

    String text = "";

    @Override
    public void render() {
        drawText(text, (int) getX(), (int) getY());
    }

    @Override
    public void tick() {
        text = getText();
        width = FontUtils.getTextWidth(text);
        height = FontUtils.getTextHeight(text);
    }

    public abstract String getText();
}
