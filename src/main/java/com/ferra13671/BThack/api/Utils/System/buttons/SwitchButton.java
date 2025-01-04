package com.ferra13671.BThack.api.Utils.System.buttons;

import com.ferra13671.BThack.Core.Render.BThackRender;

public class SwitchButton extends Button {
    private boolean bVal;

    public SwitchButton(int id, int centerX, int centerY, int width, int height, boolean defaultBVal, String text) {
        super(id, centerX, centerY, width, height, text);

        this.bVal = defaultBVal;
    }


    @Override
    public void renderButton() {
        if (!this.hovered) {
            BThackRender.drawRect(this.getCenterX() - this.getWidth(), this.getCenterY() - this.getHeight(), this.getCenterX() + this.getWidth(), this.getCenterY() + this.getHeight(), rectColor);

            BThackRender.drawString(this.getText(), this.getCenterX() - this.getHalfTextWidth(), this.getCenterY() - (mc.textRenderer.fontHeight / 2f), -1);
        } else {
            BThackRender.drawRect(this.getCenterX() - this.getWidth() - 1, this.getCenterY() - this.getHeight() - 1, this.getCenterX() + this.getWidth() + 1, this.getCenterY() + this.getHeight() + 1, rectColor);

            BThackRender.drawHorizontalGradientRect((int)(this.getCenterX() - (this.getWidth() * 0.8)), this.getCenterY() + this.getHeight() - 4, this.getCenterX(), this.getCenterY() + this.getHeight() - 2, alphaColor, whiteColor);
            BThackRender.drawHorizontalGradientRect(this.getCenterX(), this.getCenterY() + this.getHeight() - 4, (int)(this.getCenterX() + (this.getWidth() * 0.8)), this.getCenterY() + this.getHeight() - 2, whiteColor, alphaColor);
            BThackRender.drawString(this.getText(), this.getCenterX() - this.getHalfTextWidth(), this.getCenterY() - (mc.textRenderer.fontHeight / 2f), -1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (this.isMouseOnButton(mouseX, mouseY)) {
                this.bVal = !this.bVal;
            }
        }
    }

    @Override
    public String getText() {
        return this.text + ": " + (this.bVal ? "ON" : "OFF");
    }

    public float getHalfTextWidth() {
        return (mc.textRenderer.getWidth(this.getText()) / 2f);
    }

    public boolean getBVal() {
        return this.bVal;
    }
}
