package com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;

import java.util.List;

public class CloseScreenTask extends ActionBotTask {

    public CloseScreenTask() {
        super("CloseScreen");
        mode = "CloseScreen";

        taskDescription = List.of(
                "If any screen is open, it will be closed."
        );
    }

    @Override
    public void play() {
        PlayerUtils.closeHandledScreen();
    }

    @Override
    public void save(JsonObject jsonObject) {}

    @Override
    public void load(JsonObject jsonObject) {
        ActionBotConfig.tasks.add(new CloseScreenTask());
    }
}
