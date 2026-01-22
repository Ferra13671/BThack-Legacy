package com.ferra13671.BThack.impl.hud;

import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontRenderManager;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.api.module.HudComponent;

public abstract class AbstractOneTextComponent extends HudComponent {

    public AbstractOneTextComponent(float x, float y) {
        super(x, y);
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
