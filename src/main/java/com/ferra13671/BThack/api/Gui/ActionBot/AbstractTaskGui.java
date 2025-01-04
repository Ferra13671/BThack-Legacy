package com.ferra13671.BThack.api.Gui.ActionBot;

import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskSettingButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;


import java.util.ArrayList;
import java.util.List;

import static com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui.*;

public abstract class AbstractTaskGui extends BThackScreen {

    public final ArrayList<TaskSettingButton> taskSettingButtons = new ArrayList<>();

    private final boolean edit;
    private final TaskButton instance;


    protected AbstractTaskGui(TaskButton instance, boolean edit) {
        super(Text.of("Adding Task"));
        this.edit = edit;
        this.instance = instance;
    }

    @Override
    protected void init() {
        widthFactor = scaledResolution.getScaledWidth() / 37D;
        heightFactor = scaledResolution.getScaledHeight() / 30D;

        taskSettingButtons.clear();
        taskSettingButtons.addAll(getSettingButtons());

        buttons.clear();
        taskSettingButtons.forEach(taskSettingButton -> buttons.add(taskSettingButton.button));
        buttons.add(Button.of(-1, mc.getWindow().getScaledWidth() - 40, mc.getWindow().getScaledHeight() - 30, 30, 15, "Confirm"));
    }

    public abstract List<TaskSettingButton> getSettingButtons();

    public abstract ActionBotTask getAddingTask();

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawBackGround();

        taskSettingButtons.forEach(taskSettingButton -> {
            taskSettingButton.button.updateButton(mouseX, mouseY);
            taskSettingButton.render();
        });

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (activeButton.getId() == -1) {
            ActionBotTask task = getAddingTask();
            if (edit) {
                ActionBotConfig.tasks.set(instance.getId(), task);
                mc.setScreen(new ActionBotConfigGui());
            } else {
                AddingTaskGui.addTask(task);
            }
        }
        return false;
    }
}
