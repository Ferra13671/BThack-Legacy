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
        BThackRender.enableScissor(getCenterX() - getWidth() + 2, getCenterY() - getHeight(), (getWidth() * 2) - 2, getHeight() * 2);
        BThackRender.drawString(text + (insertAdd && selected ? "|" : ""), FontUtils.getTextWidth(text, FontRenderManager.DrawMode.NORMAL_BOLD) > (getWidth() * 2) - 10 ? getCenterX() + getWidth() - FontUtils.getTextWidth(text, FontRenderManager.DrawMode.NORMAL_BOLD) - 10 : (getCenterX() - getWidth() + 5), getCenterY() - (FontUtils.getTextHeight(getText(), FontRenderManager.DrawMode.NORMAL_BOLD) / 2f), ColorUtils.WHITE, true, FontRenderManager.DrawMode.NORMAL_BOLD);
        BThackRender.disableScissor();
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
        if (key == KeyboardUtils.KEY_V && KeyboardUtils.isKeyDown(KeyboardUtils.KEY_LCONTROL)) {
            String clipboard = mc.keyboard.getClipboard();
            for (int i = 0; i < clipboard.length(); i++) {
                char c = clipboard.charAt(i);
                charTyped(c);
            }
        }
    }

    @Override
    public void charTyped(char _char) {

        if (!this.selected) return;

        textBuilder.append(_char);
        if (soundTicker.passed(50)) {
            SoundSystem.playSound(Sounds.GUI_TYPING);
            soundTicker.reset();
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
