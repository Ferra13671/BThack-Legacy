package com.ferra13671.BThack.api.Utils.System.buttons;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.google.common.collect.Sets;

import java.util.Set;

public class NumberFrameButton extends Button {
    private final StringBuilder textBuilder = new StringBuilder();
    private boolean isDouble = false;

    private final Set<String> keys = Sets.newHashSet(
            "1","2","3","4","5","6","7","8","9","0"
    );


    public NumberFrameButton(int id, int centerX, int centerY, int width, int height) {
        super(id,centerX,centerY, width, height, "");
    }


    @Override
    public void renderButton() {
        BThackRender.drawRect(this.getCenterX() - this.getWidth(), this.getCenterY() - this.getHeight(), this.getCenterX() + this.getWidth(), this.getCenterY() + this.getHeight(), RECT_COLOR);

        BThackRender.drawString(textBuilder.toString(), this.getCenterX() - this.getWidth() + 3, this.getCenterY() - (FontUtils.getTextHeight(getText()) / 2f), ColorUtils.WHITE, true, FontRenderManager.DrawMode.NORMAL_BOLD);
    }


    @Override
    public void keyTyped(int key) {
        if (!selected) return;

        if (key == KeyboardUtils.KEY_BACKSPACE) {
            if (!textBuilder.isEmpty()) {
                if (Character.toString(textBuilder.charAt(textBuilder.length() - 1)).equals("."))
                    isDouble = false;
                textBuilder.deleteCharAt(textBuilder.length() - 1);
            }
        }
    }

    @Override
    public void charTyped(char _char) {
        if (!selected) return;

        String symbol = Character.toString(_char);

        if (symbol.equals("-")) {
            if (textBuilder.isEmpty()) {
                textBuilder.append(_char);
            }
            return;
        }

        if (keys.contains(symbol)) {
            if (FontUtils.getTextWidth(textBuilder.toString()) < ((this.getWidth() * 2) - ((this.getWidth() * 2) * 0.1))) {
                textBuilder.append(_char);
            }
            return;
        }

        if (symbol.equals(".")) {
            if (!isDouble && FontUtils.getTextWidth(textBuilder.toString()) < ((this.getWidth() * 2) - ((this.getWidth() * 2) * 0.1)) && !textBuilder.isEmpty()) {
                textBuilder.append(_char);
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
