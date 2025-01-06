package com.ferra13671.BThack.api.Utils.System.buttons;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;

public class Button implements Mc {

    private final int id;

    public double centerX;
    public double centerY;

    private int width;
    private int height;

    public String text;

    public boolean hovered;

    public boolean outline = false;

    public boolean hided = false;
    public boolean allowUpdate = true;

    public boolean selected = false;



    public Button(int id, int x, int y, int width, int height, String text) {
        this.id = id;

        this.centerX = x;
        this.centerY = y;

        this.width = width;
        this.height = height;

        this.text = text;
    }

    public void updateButton(int mouseX, int mouseY) {
        if (!allowUpdate) return;
        this.hovered = isMouseOnButton(mouseX, mouseY);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}


    public int rectColor = ColorUtils.fastRGBA(0, 0, 0, 76);

    public int whiteColor = ColorUtils.fastRGBA(255,255,255, 178);
    public int alphaColor = ColorUtils.fastRGBA(255,255,255,0);

    public void renderButton() {
        if (!this.hovered) {
            BThackRender.drawRect(getCenterX() - this.width, getCenterY() - this.height, getCenterX() + this.width, getCenterY() + this.height, rectColor);
        } else {
            BThackRender.drawRect(getCenterX() - this.width - 1, getCenterY() - this.height - 1, getCenterX() + this.width + 1, getCenterY() + this.height + 1, rectColor);

            BThackRender.drawHorizontalGradientRect((int)(getCenterX() - (this.width * 0.8)), getCenterY() + this.height - 4, getCenterX(), getCenterY() + this.height - 2, alphaColor, whiteColor);
            BThackRender.drawHorizontalGradientRect(getCenterX(), getCenterY() + this.height - 4, (int)(getCenterX() + (this.width * 0.8)), getCenterY() + this.height - 2, whiteColor, alphaColor);
        }
        if (outline && !selected)
            BThackRender.drawOutlineRect(getCenterX() - getWidth() - (hovered ? 2 : 0), getCenterY() - getHeight() - (hovered ? 2 : 0), getCenterX() + getWidth() + (hovered ? 2 : 0), getCenterY() + getHeight() + (hovered ? 2 : 0), 1, -1);
        BThackRender.drawString(getText(), (getCenterX() - (mc.textRenderer.getWidth(getText()) / 2f)), (getCenterY() - (mc.textRenderer.fontHeight / 2f)), -1);

        if (selected)
            BThackRender.drawOutlineRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() + getWidth(), getCenterY() + getHeight(), 1, ColorUtils.rainbow(100));
    }



    public boolean isMouseOnButton(int mouseX, int mouseY) {
        return getCenterX() - width <= mouseX && mouseX <= getCenterX() + width && getCenterY() - height <= mouseY && mouseY <= getCenterY() + height;
    }

    public void keyTyped(int key) {}

    public void charTyped(char _char) {}


    public int getId() {
        return this.id;
    }

    public int getCenterX() {
        return (int) centerX;
    }

    public int getCenterY() {
        return (int) centerY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getText() {
        if (text.startsWith("lang."))
            return LanguageSystem.translate(text);
        else
            return text;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static Button of(int id, int x, int y, int width, int height, String text) {
        return new Button(id, x, y, width, height, text);
    }
}
