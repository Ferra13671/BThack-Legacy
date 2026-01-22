package com.ferra13671.BThack.gui.screen.actionbot;

import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.core.render.utils.ColorUtils;
import com.ferra13671.BThack.core.client.systems.gui.Screen.BThackScreen;
import com.ferra13671.BThack.core.client.systems.gui.buttons.Button;
import com.ferra13671.BThack.core.client.systems.gui.buttons.NumberFrameButton;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.Utils.TaskButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

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

        this.buttons.add(new NumberFrameButton(1, (ActionBotConfigGui.scaledResolution.getScaledWidth() / 2), (int) ((ActionBotConfigGui.scaledResolution.getScaledHeight() / 2) - (ActionBotConfigGui.heightFactor * 2.5)), (int) (ActionBotConfigGui.widthFactor * 8.5), (int) ActionBotConfigGui.heightFactor));
        this.buttons.add(Button.of(-1, (ActionBotConfigGui.scaledResolution.getScaledWidth() / 2), ((ActionBotConfigGui.scaledResolution.getScaledHeight() / 2)), (int) (ActionBotConfigGui.widthFactor * 8.5), (int) ActionBotConfigGui.heightFactor, "Confirm")
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

        BThackRender.drawString("New task number", (int) ((ActionBotConfigGui.scaledResolution.getScaledWidth() / 2) - (FontUtils.getTextWidth("New task number") / 2)), (int) ((ActionBotConfigGui.scaledResolution.getScaledHeight() / 2) - (ActionBotConfigGui.heightFactor * 4.5)), ColorUtils.WHITE);

        super.render(context, mouseX, mouseY, partialTicks);
    }
}
