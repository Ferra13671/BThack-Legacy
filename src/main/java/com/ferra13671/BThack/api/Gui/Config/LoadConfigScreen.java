package com.ferra13671.BThack.api.Gui.Config;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.api.Utils.System.buttons.ButtonWithOffset;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadConfigScreen extends BThackScreen {

    private final List<Button> configs = new ArrayList<>();
    private Button selectedConfig;

    private double maxYScroll;

    private Button confirmButton;

    public LoadConfigScreen() {
        super(Text.of("Load Config"));
    }

    @Override
    protected void init() {
        buttons.clear();

        refreshConfigs();

        confirmButton = Button.of(1, mc.getWindow().getScaledWidth() - 70, mc.getWindow().getScaledHeight() - 15, 60, 10, "Load Config");
        confirmButton.hided = selectedConfig == null;

        buttons.add(confirmButton);
        buttons.add(Button.of(2, 60, mc.getWindow().getScaledHeight() - 15, 50, 10, "Refresh"));

        configs.forEach(button -> button.outline = true);
        buttons.forEach(button -> button.outline = true);
    }

    public void refreshConfigs() {
        configs.clear();

        int offset = 1;

        for (String string : ConfigSystem.getAllConfigs()) {
            ButtonWithOffset button = new ButtonWithOffset(0, mc.getWindow().getScaledWidth() / 2, 30, 75, 10, string);
            button.setOffset(offset);
            configs.add(button);
            offset++;
        }

        if (!configs.isEmpty())
            maxYScroll = configs.get(configs.size() - 1).centerY;
        else
            maxYScroll = 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawBackGround();

        BThackRender.drawRect(mc.getWindow().getScaledWidth() / 2f - 90, 0, mc.getWindow().getScaledWidth() / 2f + 90, mc.getWindow().getScaledHeight(), BACKGROUND_TABLE_COLOR);

        for (Button button : configs)
            button.renderButton();

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        for (Button button : configs) {
            button.selected = false;
            if (button.isMouseOnButton((int) mouseX, (int) mouseY)) {
                button.selected = true;
                button.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
                selectedConfig = button;
            }
        }

        if (selectedConfig != null)
            init();

        if (activeButton.getId() == confirmButton.getId()) {
            try {
                ConfigSystem.loadConfigFile(selectedConfig.getText());
            } catch (IOException ignored) {}
            mc.setScreen(BThack.instance.clickGui);
        }

        if (activeButton.getId() == 2)
            refreshConfigs();

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        Button button = configs.get(configs.size() - 1);
        if ((button.centerY + (verticalAmount * 10)) > maxYScroll)
            return false;

        for (Button button1 : configs)
            button1.centerY += (verticalAmount * 10);

        return false;
    }
}
