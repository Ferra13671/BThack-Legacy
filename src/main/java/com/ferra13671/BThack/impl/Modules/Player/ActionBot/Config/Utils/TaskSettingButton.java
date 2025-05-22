package com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.Utils;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;

public class TaskSettingButton implements Mc {

    public final Button button;
    public final String name;

    public TaskSettingButton(Button button, String name) {
        this.button = button;
        this.name = name;
    }

    public void render() {
        button.renderButton();
        BThackRender.drawString(name, button.getCenterX() + button.getWidth() + 3, button.getCenterY() - (FontUtils.getTextHeight(name) / 2), -1);
    }
}
