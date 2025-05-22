package com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.Utils;

import com.ferra13671.BThack.api.GuiSystem.buttons.Button;

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
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0)
            selected = isMouseOnButton(mouseX, mouseY);
    }

    @Override
    public int getCenterY() {
        return super.getCenterY() + ((offset * (getHeight() * 2)));
    }
}
