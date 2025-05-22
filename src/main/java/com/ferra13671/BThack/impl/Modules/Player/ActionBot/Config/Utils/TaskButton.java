package com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.Utils;



import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.ActionBotTask;

import static com.ferra13671.BThack.gui.Screen.ActionBot.ActionBotConfigGui.heightFactor;
import static com.ferra13671.BThack.gui.Screen.ActionBot.ActionBotConfigGui.widthFactor;

public class TaskButton extends Button {
    private boolean selected;
    private int offset;
    public final ActionBotTask task;


    public TaskButton(int id, String taskName, int offset, ActionBotTask task) {
        super(id ,(int) (widthFactor * 9), (int) (((heightFactor * 8) + ((heightFactor * Constants.SCREEN_ACTIONBOT_TASK_OFFSET_FACTOR) * offset)) + (heightFactor / 2)), (int) ((FontUtils.getTextWidth(taskName) / 2f) * 1.2), (int) (heightFactor / 2), taskName);

        this.offset = offset;
        this.task = task;
    }

    short alpha = 255;
    boolean alphaIvert = true;

    @Override
    public void renderButton() {
        float stringWidth = (FontUtils.getTextWidth(this.text) / 2f);

        BThackRender.drawRect((int) (this.getCenterX() - (stringWidth * 1.2)), (int) (this.getCenterY() - (heightFactor / 2)), (int) (this.getCenterX() + (stringWidth * 1.2)), (int) (this.getCenterY() + (heightFactor / 2)), ColorUtils.BLACK);
        BThackRender.drawString(this.text, (int) (this.getCenterX() - stringWidth), (int) ((heightFactor * 8) + ((heightFactor * Constants.SCREEN_ACTIONBOT_TASK_OFFSET_FACTOR) * offset) + (((heightFactor * Constants.SCREEN_ACTIONBOT_TASK_OFFSET_FACTOR) / 2) - (FontUtils.getTextHeight(text)))), ColorUtils.WHITE);

        if (selected) {
            BThackRender.drawOutlineRect((int) (this.getCenterX() - (stringWidth * 1.2) - 1), (int) (this.getCenterY() - (heightFactor / 2) - 1), (int) (this.getCenterX() + (stringWidth * 1.2) + 1), (int) (this.getCenterY() + (heightFactor / 2) + 1), 1, ColorUtils.rainbow());
        }

        if (hovered) {
            if (!alphaIvert) {
                if (alpha < 255) {
                    alpha += 3;
                } else {
                    alphaIvert = true;
                }
            } else {
                if (alpha > 1) {
                    alpha -= 3;
                } else {
                    alphaIvert = false;
                }
            }

            int hoveredColor = ColorUtils.fastRGBA(255,255,255, alpha);

            if (!selected) {
                BThackRender.drawOutlineRect((int) (this.getCenterX() - (stringWidth * 1.2) - 1), (int) (this.getCenterY() - (heightFactor / 2) - 1), (int) (this.getCenterX() + (stringWidth * 1.2) + 1), (int) (this.getCenterY() + (heightFactor / 2) + 1), 1, hoveredColor);
            } else {
                BThackRender.drawOutlineRect((int) (this.getCenterX() - (stringWidth * 1.2) - 2), (int) (this.getCenterY() - (heightFactor / 2) - 2), (int) (this.getCenterX() + (stringWidth * 1.2) + 2), (int) (this.getCenterY() + (heightFactor / 2) + 2), 1, hoveredColor);
            }
        }
    }

    @Override
    public void updateButton(int mouseX, int mouseY) {
        super.updateButton(mouseX, mouseY);


    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            selected = isMouseOnButton(mouseX, mouseY);
        }
    }


    public void setOffset(int offset) {
        this.offset = offset;

        this.setCenterY((int) (((heightFactor * 8) + ((heightFactor * Constants.SCREEN_ACTIONBOT_TASK_OFFSET_FACTOR) * offset)) + (heightFactor / 2)));
    }

    public boolean isSelected() {
        return this.selected;
    }
}
