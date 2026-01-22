package com.ferra13671.BThack.impl.modules.player.ActionBot.Config.Utils;

import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.core.client.systems.gui.buttons.Button;

public record TaskSettingButton(Button button, String name) implements Mc {

    public void render() {
        button.renderButton();
        BThackRender.drawString(name, button.getCenterX() + button.getWidth() + 3, button.getCenterY() - (FontUtils.getTextHeight(name) / 2), -1);
    }
}
