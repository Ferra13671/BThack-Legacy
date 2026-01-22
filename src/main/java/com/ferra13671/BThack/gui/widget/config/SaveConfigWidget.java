package com.ferra13671.BThack.gui.widget.config;

import com.ferra13671.BThack.core.client.systems.config.ConfigSystem;
import com.ferra13671.BThack.core.client.systems.gui.ScreenWidget;
import com.ferra13671.BThack.core.client.systems.gui.buttons.Button;
import com.ferra13671.BThack.core.client.systems.gui.buttons.TextFrameButton;
import com.ferra13671.BThack.core.client.systems.sound.Sounds;
import com.ferra13671.BThack.api.utils.KeyboardUtils;
import net.minecraft.client.gui.DrawContext;

import java.io.IOException;

public class SaveConfigWidget extends ScreenWidget {

    public SaveConfigWidget() {
        super(110, 55, 1);
    }

    @Override
    public void init() {
        super.init();

        buttons.clear();
        buttons.add(new TextFrameButton(1, (int) xLeft + 55, (int) yUp + 15, 50, 10, "Config name"));
        Button button = Button.of(2, (int) xLeft + 55, (int) yDown - 15, 50, 10, "Confirm")
                .withAction(buttonClickInfo -> {
                    try {
                        ConfigSystem.saveConfigFile(getButtonFromId(1).getText());
                    } catch (IOException ignored) {}
                    close();
                });
        button.setClickSound(Sounds.CONFIG_SAVED_OR_LOADED);
        buttons.add(button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawPlate();
        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        if (keyCode == KeyboardUtils.KEY_ESCAPE) close();
        return super.keyPressed(keyCode, scanCode, shift);
    }

    @Override
    public void close() {
        super.close();
        parent.widgetManage.addWidget(new ConfigsWidget());
    }
}
