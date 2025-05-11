package com.ferra13671.BThack.api.Gui.Screen.ActionBot;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.GuiSystem.Screen.BThackScreen;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.BThack.api.GuiSystem.buttons.NumberFrameButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import static com.ferra13671.BThack.api.Gui.Screen.ActionBot.ActionBotConfigGui.*;

public class MoveTaskGui extends BThackScreen {
    private final TaskButton taskButton;

    public MoveTaskGui(TaskButton taskButton) {
        super(Text.literal("MoveTask"));
        this.taskButton = taskButton;
    }

    @Override
    public void init() {
        super.init();
        this.buttons.clear();

        this.buttons.add(new NumberFrameButton(1, (scaledResolution.getScaledWidth() / 2), (int) ((scaledResolution.getScaledHeight() / 2) - (heightFactor * 2.5)), (int) (widthFactor * 8.5), (int) heightFactor));
        this.buttons.add(Button.of(-1, (scaledResolution.getScaledWidth() / 2), ((scaledResolution.getScaledHeight() / 2)), (int) (widthFactor * 8.5), (int) heightFactor, "Confirm")
                .withAction(buttonClickInfo -> {
                    int number = (int) ((NumberFrameButton) getButtonFromId(1)).getNumber();
                    if (number <= 0) number = 1;

                    ActionBotConfig.tasks.remove(this.taskButton.getId());
                    ActionBotConfig.tasks.add(Math.max(Math.min(ActionBotConfig.tasks.size() - 2, number), 1), taskButton.task);

                    mc.setScreen(new ActionBotConfigGui());
                }));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawBackGround(mouseX, mouseY);

        BThackRender.drawString("New task number", (int) ((scaledResolution.getScaledWidth() / 2) - (FontUtils.getTextWidth("New task number") / 2)), (int) ((scaledResolution.getScaledHeight() / 2) - (heightFactor * 4.5)), ColorUtils.WHITE);

        super.render(context, mouseX, mouseY, partialTicks);
    }
}
