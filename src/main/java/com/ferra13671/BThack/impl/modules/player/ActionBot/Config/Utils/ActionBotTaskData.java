package com.ferra13671.BThack.impl.modules.player.ActionBot.Config.Utils;

import com.ferra13671.BThack.core.client.systems.gui.Screen.BThackScreen;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTask;

public interface ActionBotTaskData {

    ActionBotTask getTask();

    BThackScreen getTaskScreen(TaskButton instance, boolean edit);
}
