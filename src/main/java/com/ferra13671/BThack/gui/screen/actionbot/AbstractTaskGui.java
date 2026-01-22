package com.ferra13671.BThack.gui.screen.actionbot;

import com.ferra13671.BThack.core.client.systems.gui.Screen.BThackScreen;
import com.ferra13671.BThack.core.client.systems.gui.buttons.Button;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.Utils.TaskButton;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.Utils.TaskSettingButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;


import java.util.ArrayList;
import java.util.List;

import static com.ferra13671.BThack.gui.screen.actionbot.ActionBotConfigGui.*;

public abstract class AbstractTaskGui extends BThackScreen {

    public final ArrayList<TaskSettingButton> taskSettingButtons = new ArrayList<>();

    private final boolean edit;
    private final TaskButton instance;


    protected AbstractTaskGui(TaskButton instance, boolean edit) {
        super(Text.literal("Adding Task"));
        this.edit = edit;
        this.instance = instance;
    }

    @Override
    protected void init() {
        super.init();
        widthFactor = scaledResolution.getScaledWidth() / 37D;
        heightFactor = scaledResolution.getScaledHeight() / 30D;

        taskSettingButtons.clear();
        taskSettingButtons.addAll(getSettingButtons());

        buttons.clear();
        taskSettingButtons.forEach(taskSettingButton -> buttons.add(taskSettingButton.button()));
        buttons.add(Button.of(-1, mc.getWindow().getScaledWidth() - 40, mc.getWindow().getScaledHeight() - 30, 30, 15, "Confirm")
                .withAction(buttonClickInfo -> {
                    ActionBotTask task = getAddingTask();
                    if (edit) {
                        ActionBotConfig.tasks.set(instance.getId(), task);
                        mc.setScreen(new ActionBotConfigGui());
                    } else {
                        AddingTaskGui.addTask(task);
                    }
                }));
    }

    public abstract List<TaskSettingButton> getSettingButtons();

    public abstract ActionBotTask getAddingTask();

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawBackGround(mouseX, mouseY);

        taskSettingButtons.forEach(taskSettingButton -> {
            taskSettingButton.button().updateButton(mouseX, mouseY);
            taskSettingButton.render();
        });

        super.render(context, mouseX, mouseY, partialTicks);
    }
}
