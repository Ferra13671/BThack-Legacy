package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.api.Interfaces.Pc;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;

import java.util.List;

public class CloseScreenTask extends ActionBotTask implements Pc {

    public CloseScreenTask() {
        super("CloseScreen");
        mode = "CloseScreen";

        taskDescription = List.of(
                "If any screen is open, it will be closed."
        );
    }

    @Override
    public void play() {
        pc.closeScreen();
    }

    @Override
    public void save(JsonObject jsonObject) {
        //No action
    }

    @Override
    public void load(JsonObject jsonObject) {
        ActionBotConfig.tasks.add(new CloseScreenTask());
    }
}
