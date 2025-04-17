package com.ferra13671.BThack.api.GuiSystem.buttons;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import net.minecraft.util.Formatting;

public class TextFrameButton extends Button {
    private final String nullText;
    private StringBuilder textBuilder = new StringBuilder();

    private boolean selected = false;
    private final Ticker insertTicker = new Ticker();
    private boolean insertAdd = false;
    private final Ticker soundTicker = new Ticker();


    public TextFrameButton(int id, int centerX, int centerY, int width, int height) {
        super(id,centerX,centerY, width, height, "");
        this.nullText = "";
    }

    public TextFrameButton(int id, int centerX, int centerY, int width, int height, String nullText) {
        super(id,centerX,centerY, width, height, "");
        this.nullText = nullText;
    }

    @Override
    public void updateButton(int mouseX, int mouseY) {
        if (selected && insertTicker.passed(500)) {
            insertAdd = !insertAdd;
            insertTicker.reset();
        } else if (!selected) insertAdd = false;
    }

    @Override
    public void renderButton() {
        drawPlate(getAnimationDelta());
        String text = textBuilder.toString();
        if (text.isEmpty() && !selected) text = Formatting.GRAY + nullText + "...";
        BThackRender.drawString(text + (insertAdd && selected ? "|" : ""), getCenterX() - getWidth() + 5, getCenterY() - (FontUtils.getTextHeight(getText(), FontRenderManager.DrawMode.NORMAL_BOLD) / 2f), ColorUtils.WHITE, true, FontRenderManager.DrawMode.NORMAL_BOLD);
    }

    @Override
    public void keyTyped(int key) {

        if (!this.selected) return;

        if (key == KeyboardUtils.KEY_BACKSPACE) {
            if (!textBuilder.isEmpty()) {
                textBuilder.deleteCharAt(textBuilder.length() - 1);
                if (soundTicker.passed(50)) {
                    SoundSystem.playSound(Sounds.GUI_TYPING);
                    soundTicker.reset();
                }
            }
        }
    }

    @Override
    public void charTyped(char _char) {

        if (!this.selected) return;

        if (FontUtils.getTextWidth(textBuilder.toString()) < ((getWidth() * 2) - ((getWidth() * 2) * 0.1))) {
            textBuilder.append(_char);
            if (soundTicker.passed(50)) {
                SoundSystem.playSound(Sounds.GUI_TYPING);
                soundTicker.reset();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            selected = isMouseOnButton(mouseX, mouseY);
    }

    @Override
    public String getText() {
        return textBuilder.toString();
    }

    public void setText(String text) {
        textBuilder = new StringBuilder(text);
    }
}
