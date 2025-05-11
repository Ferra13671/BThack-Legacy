package com.ferra13671.BThack.api.GuiSystem.buttons;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Font.FontUtils;

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
        drawPlate(getAnimationDelta());
        BThackRender.drawString(this.getText(), (int) (this.getCenterX() - this.getHalfTextWidth()), this.getCenterY() - (FontUtils.getTextHeight(getText()) / 2f), -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);
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
        return (FontUtils.getTextWidth(this.getText()) / 2f);
    }
}
