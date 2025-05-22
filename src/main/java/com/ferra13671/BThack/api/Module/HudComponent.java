package com.ferra13671.BThack.api.Module;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.impl.Modules.CLIENT.HUD;

public abstract class HudComponent extends Module {
    private float x; //Left edge
    private float y; //Upper edge

    private int scaledWidth;
    private int scaledHeight;

    public float width;  //Right
    public float height; //Down

    public HudComponent(float x, float y) {
        setX(x, mc.getWindow().getScaledWidth());
        setY(y, mc.getWindow().getScaledHeight());
        if (isAutoEnabled())
            setEnabled(true);
    }

    @Override
    public boolean isAllowRemapVisible() {
        return false;
    }

    @Override
    public boolean isAllowRemapKeyCode() {
        return false;
    }

    public void setX(float value, int scaledWidth) {
        this.x = value;
        this.scaledWidth = scaledWidth;
    }

    public void setY(float value, int scaledHeight) {
        this.y = value;
        this.scaledHeight = scaledHeight;
    }

    public float getX() {
        float factor = (this.x / scaledWidth) * 100;
        return (mc.getWindow().getScaledWidth() / 100f) * factor;
    }

    @Override
    protected final void addToArrayList() {}

    @Override
    protected final void removeFromArrayList() {}

    public float getY() {
        return this.y;
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }

    public float getNoScaledX() {
        return this.x;
    }

    public float getNoScaledY() {
        return this.y;
    }

    public abstract void render();

    public void tick() {}

    public void drawText(String text, float x, float y, int color) {
        BThackRender.drawString(text, x, y, color, true, FontRenderManager.DrawMode.NORMAL_BOLD);
    }

    public void drawText(String text, float x, float y) {
        drawText(text, x, y, HUD.getHUDColor());
    }

    @Override
    public void playOnSound() {
        //No action
    }

    @Override
    public void playOffSound() {
        //No action
    }

    @Override
    public void sendToggleMessage() {
        //No action
    }
}
