package com.ferra13671.BThack.api.HudComponent;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.HUD;

import java.text.DecimalFormat;

public abstract class HudComponent extends Module {
    private float x; //Left edge
    private float y; //Upper edge

    private int scaledWidth;
    private int scaledHeight;

    public float width;  //Right
    public float height; //Down

    public final DecimalFormat decimal = new DecimalFormat("0.00");

    public HudComponent(String name, float x, float y, boolean autoToggled) {
        super(name, "", KeyboardUtils.RELEASE, Categories.HUD, autoToggled);
        allowRemapKeyCode = false;

        setX(x, mc.getWindow().getScaledWidth());
        setY(y, mc.getWindow().getScaledHeight());
        if (autoToggled)
            setToggled(true);

        allowRemapVisible = false;
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

    public float getY() {
        //float factor = (this.y / scaledHeight) * 100;
        //return (mc.getWindow().getScaledHeight() / 100f) * factor;
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

    public void drawText(String text, int x, int y, int color) {
        BThackRender.drawString(text, x, y, color);
    }

    public void drawText(String text, int x, int y) {
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
