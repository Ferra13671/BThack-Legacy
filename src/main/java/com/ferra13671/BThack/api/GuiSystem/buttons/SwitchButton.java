package com.ferra13671.BThack.api.GuiSystem.buttons;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;

public class SwitchButton extends Button {
    private boolean bVal;

    public SwitchButton(int id, int centerX, int centerY, int width, int height, boolean defaultBVal, String text) {
        super(id, centerX, centerY, width, height, text);

        this.bVal = defaultBVal;
    }


    @Override
    public void renderButton() {
        drawPlate(getAnimationDelta());
        BThackRender.drawString(this.getText(), this.getCenterX() - this.getHalfTextWidth(), this.getCenterY() - (FontUtils.getTextHeight(getText()) / 2f), -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            if (isMouseOnButton(mouseX, mouseY)) {
                this.bVal = !this.bVal;
            }
        }
    }

    @Override
    public String getText() {
        return this.text + ": " + (this.bVal ? "ON" : "OFF");
    }

    public float getHalfTextWidth() {
        return (FontUtils.getTextWidth(this.getText()) / 2f);
    }

    public boolean getBVal() {
        return this.bVal;
    }
}
