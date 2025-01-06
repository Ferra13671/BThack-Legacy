package com.ferra13671.BThack.api.Gui.ActionBot;



import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.ActionBotTaskData;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.AddingTaskButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;

import static com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui.*;

public class AddingTaskGui extends BThackScreen {
    private String taskName = "Select the desired task";


    private AddingTaskButton preSelectTask;


    private final ArrayList<AddingTaskButton> taskButtons = new ArrayList<>();

    private double maxYScroll;

    protected static TaskButton substituteTask;

    public AddingTaskGui() {
        super(Text.of("AddingTask"));
    }


    @Override
    public void init() {

        buttons.clear();
        taskButtons.clear();

        int offset = 1;
        for (ActionBotTaskData ac : ActionBotConfig.getFullActionBotTasks()) {
            ActionBotTask task = ac.getTask();
            AddingTaskButton addingTaskButton = new AddingTaskButton(0, getX100P() * 13, 15,
                    getX100P() * 10, 10, task.mode, ac);
            addingTaskButton.setOffset(offset);
            offset++;
            taskButtons.add(addingTaskButton);
        }
        maxYScroll = taskButtons.get(taskButtons.size() - 1).centerY;

        Button setupButton = Button.of(-1, (scaledResolution.getScaledWidth() - 60), (scaledResolution.getScaledHeight() - 30), 40, 10, "Setup");
        setupButton.outline = true;
        buttons.add(setupButton);
        buttons.forEach(button -> button.outline = true);
        taskButtons.forEach(buttons -> buttons.outline = true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawBackGround();

        BThackRender.drawRect(getX100P() * 25, 20, (scaledResolution.getScaledWidth() - 10), (scaledResolution.getScaledHeight() - 50), BACKGROUND_TABLE_COLOR);
        BThackRender.drawString(taskName, ((scaledResolution.getScaledWidth() - 60) - mc.textRenderer.getWidth(taskName)), 25, ColorUtils.WHITE);

        short offset = 0;

        if (preSelectTask != null) {
            for (String text : preSelectTask.task.getTask().getTaskDescription()) {
                if (!text.equals("NULL")) {
                    BThackRender.drawString(text, getX100P() * 27,  (40 + (10 * offset)), -1);
                    offset++;
                }
            }
        }

        for (AddingTaskButton taskButton : taskButtons) {
            taskButton.updateButton(mouseX, mouseY);
            taskButton.renderButton();
        }

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        Button button = taskButtons.get(taskButtons.size() - 1);
        if ((button.centerY + (verticalAmount * 10)) > maxYScroll)
            return false;

        for (AddingTaskButton tButton : taskButtons) {
            tButton.centerY += (verticalAmount * 10);
        }

        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        taskButtons.forEach(button -> button.selected = false);

        for (AddingTaskButton button : taskButtons) {
            if (button.isMouseOnButton((int) mouseX, (int) mouseY)) {
                button.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
            }
            if (button.selected) {
                preSelectTask = button;
                updateTaskName();
                return false;
            }
        }

        if (activeButton.getId() == -1) {
            mc.setScreen(preSelectTask.task.getTaskScreen(null, false));
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void updateTaskName() {
        taskName = preSelectTask.task.getTask().getName() + " Task";
    }


    public static void addTask(ActionBotTask task) {
        if (task != null) {
            if (substituteTask != null) {
                ActionBotConfig.tasks.add(AddingTaskGui.substituteTask.getId(), task);
            } else {
                ActionBotConfig.addActionTaskToList(task);
            }
        }

         mc.setScreen(new ActionBotConfigGui());
    }
}
