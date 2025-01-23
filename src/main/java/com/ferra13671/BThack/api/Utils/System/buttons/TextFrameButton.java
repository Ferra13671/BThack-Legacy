package com.ferra13671.BThack.api.Utils.System.buttons;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class TextFrameButton extends Button {
    private StringBuilder textBuilder = new StringBuilder();

    private boolean selected = false;


    public TextFrameButton(int id, int centerX, int centerY, int width, int height) {
        super(id,centerX,centerY, width, height, "");
    }


    @Override
    public void renderButton() {
        BThackRender.drawRect(this.getCenterX() - this.getWidth(), this.getCenterY() - this.getHeight(), this.getCenterX() + this.getWidth(), this.getCenterY() + this.getHeight(), RECT_COLOR);

        BThackRender.drawString(textBuilder.toString(), this.getCenterX() - this.getWidth() + 3, this.getCenterY() - (FontUtils.getTextHeight(getText()) / 2f), ColorUtils.WHITE, true, FontRenderManager.DrawMode.NORMAL_BOLD);
    }

    @Override
    public void keyTyped(int key) {

        if (!this.selected) return;

        if (key == KeyboardUtils.KEY_BACKSPACE) {
            if (!textBuilder.isEmpty()) {
                textBuilder.deleteCharAt(textBuilder.length() - 1);
            }
        }
    }

    @Override
    public void charTyped(char _char) {

        if (!this.selected) return;

        if (FontUtils.getTextWidth(textBuilder.toString()) < ((this.getWidth() * 2) - ((this.getWidth() * 2) * 0.1))) {
            textBuilder.append(_char);
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
