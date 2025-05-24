package com.ferra13671.BThack.api.GuiSystem.buttons;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.google.common.collect.Sets;
import net.minecraft.util.Formatting;

import java.util.Set;

public class NumberFrameButton extends Button {
    private final String nullText;
    private final StringBuilder textBuilder = new StringBuilder();
    private boolean isDouble = false;

    private boolean selected = false;
    private final Ticker soundTicker = new Ticker();
    private final Ticker insertTicker = new Ticker();
    private boolean insertAdd = false;

    private final Set<String> keys = Sets.newHashSet(
            "1","2","3","4","5","6","7","8","9","0"
    );

    public NumberFrameButton(int id, int centerX, int centerY, int width, int height) {
        super(id,centerX,centerY, width, height, "");
        this.nullText = "";
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
        if (!selected) return;

        if (key == KeyboardUtils.KEY_BACKSPACE) {
            if (!textBuilder.isEmpty()) {
                if (Character.toString(textBuilder.charAt(textBuilder.length() - 1)).equals("."))
                    isDouble = false;
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
        if (!selected) return;

        String symbol = Character.toString(_char);

        if (symbol.equals("-")) {
            if (textBuilder.isEmpty()) {
                if (soundTicker.passed(50)) {
                    SoundSystem.playSound(Sounds.GUI_TYPING);
                    soundTicker.reset();
                }
                textBuilder.append(_char);
            }
            return;
        }

        if (keys.contains(symbol)) {
            if (FontUtils.getTextWidth(textBuilder.toString()) < ((this.getWidth() * 2) - ((this.getWidth() * 2) * 0.1))) {
                if (soundTicker.passed(50)) {
                    SoundSystem.playSound(Sounds.GUI_TYPING);
                    soundTicker.reset();
                }
                textBuilder.append(_char);
            }
            return;
        }

        if (symbol.equals(".")) {
            if (!isDouble && !textBuilder.isEmpty()) {
                textBuilder.append(_char);
                if (soundTicker.passed(50)) {
                    SoundSystem.playSound(Sounds.GUI_TYPING);
                    soundTicker.reset();
                }
                isDouble = true;
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

    public double getNumber() {
        if (!textBuilder.isEmpty()) {
            if (textBuilder.charAt(textBuilder.length() - 1) == '.') {
                textBuilder.append("0");
            }
        }
        if (textBuilder.isEmpty()) {
            return 0;
        }

        return Double.parseDouble(textBuilder.toString());
    }
}
