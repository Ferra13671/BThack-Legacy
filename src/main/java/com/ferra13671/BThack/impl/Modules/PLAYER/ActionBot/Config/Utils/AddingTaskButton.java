package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;

public class AddingTaskButton extends Button {
    public boolean selected = false;
    public ActionBotTaskData task;

    private int offset;

    public AddingTaskButton(int id, int x, int y, int width, int height, String taskName, ActionBotTaskData task) {
        super(id, x, y, width, height, taskName);


        this.task = task;
    }

    public void setOffset(int newOffset) {
        offset = newOffset;
    }

    @Override
    public void renderButton() {
        super.renderButton();

        if (selected)
            BThackRender.drawOutlineRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() + getWidth(), getCenterY() + getHeight(), 1, ColorUtils.rainbow(100));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            selected = true;
    }

    @Override
    public int getCenterY() {
        return (int) centerY + ((offset * (getHeight() * 2)));
    }
}
