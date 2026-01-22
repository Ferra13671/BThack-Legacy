package com.ferra13671.BThack.core.client.systems.gui.buttons;

import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontRenderManager;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.core.render.utils.ColorUtils;
import com.ferra13671.BThack.api.animation.Animation;
import com.ferra13671.BThack.api.animation.Easing;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.core.client.systems.gui.ButtonClickInfo;
import com.ferra13671.BThack.core.client.systems.sound.Sound;
import com.ferra13671.BThack.core.client.systems.sound.SoundSystem;
import com.ferra13671.BThack.core.client.systems.sound.Sounds;
import com.ferra13671.BThack.Constants;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;

import java.util.function.Consumer;

public class Button implements Mc {
    private final int id;

    protected int centerX;
    protected int centerY;
    protected int width;
    protected int height;
    public String text;
    protected boolean hovered;
    protected boolean hided = false;
    protected boolean allowUpdate = true;
    protected boolean selected = false;
    protected Consumer<ButtonClickInfo> clickConsumer = null;
    protected final Animation hoveredAnimation = new Animation(Easing.LINEAR, 200);

    protected Sound clickSound = Sounds.BUTTON_CLICK;



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

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOnButton(mouseX, mouseY) && clickSound != null)
            SoundSystem.playSound(clickSound);
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

    public void renderButton() {
        float animationDelta = getAnimationDelta();
        drawPlate(animationDelta);
        BThackRender.drawString(getText(), (getCenterX() - (FontUtils.getTextWidth(getText(), FontRenderManager.DrawMode.NORMAL_BOLD) / 2f)), (getCenterY() - (FontUtils.getTextHeight(getText(), FontRenderManager.DrawMode.NORMAL_BOLD) / 2f)), -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);
    }

    protected void drawPlate(float animationDelta) {
        animationDelta *= 2;
        if (!hovered && hoveredAnimation.getEase() >= 1) {
            BThackRender.drawRoundedRectWithOutline(getCenterX() - width, getCenterY() - height, getCenterX() + width, getCenterY() + height, 10f, Constants.GUISYSTEM_BUTTON_RECT_COLOR, selected ? ColorUtils.rainbow() : -1, 1);
        } else {
            BThackRender.drawRoundedRectWithOutline(getCenterX() - width - animationDelta, getCenterY() - height - animationDelta, getCenterX() + width + animationDelta, getCenterY() + height + animationDelta, 10f, Constants.GUISYSTEM_BUTTON_RECT_COLOR, selected ? ColorUtils.rainbow() : -1, 1);

            drawHoveredLight(animationDelta / 2);
        }
    }

    protected void drawHoveredLight(float animationDelta) {
        BThackRender.drawHorizontalGradientRect((int)(getCenterX() - (width * 0.8 * animationDelta)), getCenterY() + height - 4, getCenterX(), getCenterY() + height - 2, ColorUtils.TRANSPARENT, ColorUtils.integrateAlpha(Constants.GUISYSTEM_BUTTON_HOVERED_LIGHT_COLOR, (int) (animationDelta * 255)));
        BThackRender.drawHorizontalGradientRect(getCenterX(), getCenterY() + height - 4, (int)(getCenterX() + (width * 0.8 * animationDelta)), getCenterY() + height - 2, ColorUtils.integrateAlpha(Constants.GUISYSTEM_BUTTON_HOVERED_LIGHT_COLOR, (int) (animationDelta * 255)), ColorUtils.TRANSPARENT);
    }

    public float getAnimationDelta() {
        return (float) (hovered ? hoveredAnimation.getEase() : 1 - hoveredAnimation.getEase());
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
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isHovered() {
        return hovered;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isHided() {
        return hided;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getText() {
        if (text.startsWith("lang."))
            return LanguageSystem.translate(text);
        else
            return text;
    }

    public void setClickSound(Sound clickSound) {
        this.clickSound = clickSound;
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

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public void setHided(boolean hided) {
        this.hided = hided;
    }

    public void setAllowUpdate(boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
