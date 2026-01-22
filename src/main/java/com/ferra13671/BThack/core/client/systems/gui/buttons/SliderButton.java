package com.ferra13671.BThack.core.client.systems.gui.buttons;


import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontRenderManager;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.core.render.utils.ColorUtils;
import com.ferra13671.BThack.core.client.systems.sound.SoundSystem;
import com.ferra13671.BThack.core.client.systems.sound.Sounds;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.utils.MathUtils;
import com.ferra13671.BThack.api.utils.Ticker;

public class SliderButton extends Button {

    public boolean dragging = false;
    public double value;
    public final double min;
    public final double max;

    private double renderWidth = -1;
    private final Ticker soundTicker = new Ticker();

    public SliderButton(int id, int x, int y, int width, int height, String text, double defaultValue, double min, double max) {
        super(id, x, y, width, height, text);
        this.min = min;
        this.max = max;
        value = defaultValue;
    }

    @Override
    public void renderButton() {
        BThackRender.drawRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() + getWidth(), getCenterY() + getHeight(), Constants.GUISYSTEM_BUTTON_RECT_COLOR);

        BThackRender.drawRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() - getWidth() + (int) renderWidth, getCenterY() + getHeight(), ColorUtils.fastRGBA(255,255,255,100));

        BThackRender.drawOutlineRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() + getWidth(), getCenterY() + getHeight(), 1, ColorUtils.WHITE);

        BThackRender.drawString(getText() + ": " + value, getCenterX() - getWidth() + 3, getCenterY() - (int) (FontUtils.getTextHeight(getText()) / 2f), ColorUtils.WHITE, true, FontRenderManager.DrawMode.NORMAL_BOLD);
    }

    @Override
    public void updateButton(int mouseX, int mouseY) {
        int x = getCenterX() - getWidth();

        //double diff = Math.min((getWidth()), Math.max(0, mouseX - x));
        double diff = ((mouseX - x) / (getWidth() * 2d)) * 100;
        diff = Math.max(0, Math.min(100, diff));

        double prevRenderWidth = renderWidth;

        renderWidth = (getWidth() * 2) * ((value - min) / (max - min));

        if (prevRenderWidth != renderWidth && prevRenderWidth != -1 && soundTicker.passed(50)) {
            SoundSystem.playSound(renderWidth > prevRenderWidth ? Sounds.GUI_SLIDER_UP : Sounds.GUI_SLIDER_DOWN);
            soundTicker.reset();
        }

        if (dragging) {
            if (diff == 0) {
                value = min;
            } else {
                value = MathUtils.roundNumber(((diff / 100) * (max - min) + min), Constants.CLICKGUI_SLIDER_ROUND_TO_PLACE_VALUE);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOnButton(mouseX, mouseY)) {
                dragging = true;
                updateButton(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            dragging = false;
    }
}
