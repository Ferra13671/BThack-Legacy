package com.ferra13671.BThack.gui.Screen.ActionBot;



import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.GuiSystem.Screen.BThackScreen;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.Utils.ActionBotTaskData;
import com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.Utils.AddingTaskButton;
import com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.Utils.TaskButton;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class AddingTaskGui extends BThackScreen {
    private String taskName = "Select the desired task";


    private AddingTaskButton preSelectTask;


    private final ArrayList<AddingTaskButton> taskButtons = new ArrayList<>();

    private double maxYScroll;

    protected static TaskButton substituteTask;

    public AddingTaskGui() {
        super(Text.literal("AddingTask"));
    }


    @Override
    public void init() {
        super.init();

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
        maxYScroll = taskButtons.getLast().getCenterY();

        Button setupButton = Button.of(-1, (ActionBotConfigGui.scaledResolution.getScaledWidth() - 60), (ActionBotConfigGui.scaledResolution.getScaledHeight() - 30), 40, 10, "Setup")
                .withAction(buttonClickInfo -> mc.setScreen(preSelectTask.task.getTaskScreen(null, false)));
        buttons.add(setupButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawBackGround(mouseX, mouseY);

        BThackRender.drawRect(getX100P() * 25, 20, (ActionBotConfigGui.scaledResolution.getScaledWidth() - 10), (ActionBotConfigGui.scaledResolution.getScaledHeight() - 50), Constants.SCREEN_BACKGROUND_TABLE_COLOR);
        BThackRender.drawString(taskName, ((ActionBotConfigGui.scaledResolution.getScaledWidth() - 60) - FontUtils.getTextWidth(taskName)), 25, ColorUtils.WHITE);

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
        Button button = taskButtons.getLast();
        if ((button.getCenterY() + (verticalAmount * 10)) > maxYScroll)
            return false;

        for (AddingTaskButton tButton : taskButtons) {
            tButton.setCenterY((int) (tButton.getCenterY() + (verticalAmount * 10)));
        }

        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
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
