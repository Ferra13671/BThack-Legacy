package com.ferra13671.BThack.api.Utils.System.buttons;

import com.ferra13671.BThack.Core.Render.BThackRender;

import java.util.ArrayList;

public class ModeButton extends Button {
    private final ArrayList<String> options;

    int index = 0;
    String sVal;

    public ModeButton(int id, int centerX, int centerY, int width, int height, String text, ArrayList<String> options) {
        super(id, centerX, centerY, width, height, text);

        this.options = options;

        this.sVal = this.options.get(index);
    }

    @Override
    public void renderButton() {
        if (!this.hovered) {
            BThackRender.drawRect(this.getCenterX() - this.getWidth(), this.getCenterY() - this.getHeight(), this.getCenterX() + this.getWidth(), this.getCenterY() + this.getHeight(), rectColor);

            BThackRender.drawString(this.getText(), (int) (this.getCenterX() - this.getHalfTextWidth()), this.getCenterY() - (mc.textRenderer.fontHeight / 2f), -1);
        } else {
            BThackRender.drawRect(this.getCenterX() - this.getWidth() - 1, this.getCenterY() - this.getHeight() - 1, this.getCenterX() + this.getWidth() + 1, this.getCenterY() + this.getHeight() + 1, rectColor);

            BThackRender.drawHorizontalGradientRect((int)(this.getCenterX() - (this.getWidth() * 0.8)), this.getCenterY() + this.getHeight() - 4, this.getCenterX(), this.getCenterY() + this.getHeight() - 2, alphaColor, whiteColor);
            BThackRender.drawHorizontalGradientRect(this.getCenterX(), this.getCenterY() + this.getHeight() - 4, (int)(this.getCenterX() + (this.getWidth() * 0.8)), this.getCenterY() + this.getHeight() - 2, whiteColor, alphaColor);
            BThackRender.drawString(this.getText(), (int) (this.getCenterX() - this.getHalfTextWidth()), this.getCenterY() - (mc.textRenderer.fontHeight / 2f), -1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (!isMouseOnButton(mouseX, mouseY)) return;

        if (index >= (options.size() - 1))
            index = 0;
        else
            index++;

        this.sVal = options.get(index);
    }

    @Override
    public String getText() {
        return this.text + ": " + this.sVal;
    }


    public String getsVal() {
        return this.sVal;
    }

    public float getHalfTextWidth() {
        return (mc.textRenderer.getWidth(this.getText()) / 2f);
    }
}
