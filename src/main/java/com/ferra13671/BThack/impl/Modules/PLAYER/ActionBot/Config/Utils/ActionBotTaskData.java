package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils;

import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;

public interface ActionBotTaskData {

    ActionBotTask getTask();

    BThackScreen getTaskScreen(TaskButton instance, boolean edit);
}
