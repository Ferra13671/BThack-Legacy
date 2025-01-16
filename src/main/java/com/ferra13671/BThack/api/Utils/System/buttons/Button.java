package com.ferra13671.BThack.api.Utils.System.buttons;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.System.ButtonClickInfo;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;

import java.util.function.Consumer;

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
    private Consumer<ButtonClickInfo> clickConsumer = null;
    private final Animation hoveredAnimation = new Animation(Easing.LINEAR, 200);



    public Button(int id, int x, int y, int width, int height, String text) {
        this.id = id;

        this.centerX = x;
        this.centerY = y;

        this.width = width;
        this.height = height;

        this.text = text;
        hoveredAnimation.setStartMillis(-1);
    }

    public void updateButton(int mouseX, int mouseY) {
        if (!allowUpdate) return;
        boolean prevHovered = hovered;
        hovered = isMouseOnButton(mouseX, mouseY);
        if (hovered != prevHovered) hoveredAnimation.reset();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}


    public int rectColor = ColorUtils.fastRGBA(0, 0, 0, 76);

    public int whiteColor = ColorUtils.fastRGBA(255,255,255, 178);
    public int alphaColor = ColorUtils.fastRGBA(255,255,255,0);

    public void renderButton() {
        float animationDelta = (float) (hovered ? hoveredAnimation.getEase() : 1 - hoveredAnimation.getEase());
        if (!hovered && hoveredAnimation.getEase() >= 1) {
            BThackRender.drawRect(getCenterX() - width, getCenterY() - height, getCenterX() + width, getCenterY() + height, rectColor);
        } else {
            BThackRender.drawRect(getCenterX() - width - 1, getCenterY() - height - animationDelta, getCenterX() + width + animationDelta, getCenterY() + height + 1, rectColor);

            BThackRender.drawHorizontalGradientRect((int)(getCenterX() - (width * 0.8 * animationDelta)), getCenterY() + height - 4, getCenterX(), getCenterY() + height - 2, alphaColor, ColorUtils.integrateAlpha(whiteColor, (int) (animationDelta * 255)));
            BThackRender.drawHorizontalGradientRect(getCenterX(), getCenterY() + height - 4, (int)(getCenterX() + (width * 0.8 * animationDelta)), getCenterY() + height - 2, ColorUtils.integrateAlpha(whiteColor, (int) (animationDelta * 255)), alphaColor);
        }
        if (outline && !selected)
            BThackRender.drawOutlineRect(getCenterX() - getWidth() - (animationDelta * 2), getCenterY() - getHeight() - (animationDelta * 2), getCenterX() + getWidth() + (animationDelta * 2), getCenterY() + getHeight() + (animationDelta * 2), 1, -1);
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

    public void clickAction(int mouseX, int mouseY, int mouseButton) {
        if (clickConsumer != null)
            clickConsumer.accept(new ButtonClickInfo(mouseX, mouseY, mouseButton));
    }

    public Button withAction(Consumer<ButtonClickInfo> clickConsumer) {
        this.clickConsumer = clickConsumer;
        return this;
    }

    public static Button of(int id, int x, int y, int width, int height, String text) {
        return new Button(id, x, y, width, height, text);
    }
}
