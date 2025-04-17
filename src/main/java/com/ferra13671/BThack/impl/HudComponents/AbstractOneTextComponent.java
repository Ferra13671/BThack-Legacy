package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.Module.HudComponent;

public abstract class AbstractOneTextComponent extends HudComponent {

    public AbstractOneTextComponent(String name, float x, float y, boolean autoToggled) {
        super(name, x, y, autoToggled);
    }

    String text = "";

    @Override
    public void render() {
        BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);
        drawText(text, getX() + 3, getY() + 3);
    }

    @Override
    public void tick() {
        text = getText();
        width = FontUtils.getTextWidth(text, FontRenderManager.DrawMode.NORMAL_BOLD) + 6;
        height = FontUtils.getTextHeight(text, FontRenderManager.DrawMode.NORMAL_BOLD) + 6;
    }

    public abstract String getText();
}
