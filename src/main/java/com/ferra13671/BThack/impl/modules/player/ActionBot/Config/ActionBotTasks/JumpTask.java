package com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTasks;


import com.ferra13671.BThack.managers.impl.thread.ThreadClosedException;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;

import java.util.List;

public class JumpTask extends ActionBotTask {

    public JumpTask() {
        super("Jump");
        this.mode = "Jump";

        this.taskDescription = List.of(
                "When the task is activated, the player starts jumping."
        );
    }


    @Override
    @SuppressWarnings("DataFlowIssue")
    public void play() throws ThreadClosedException {
        mc.player.jump();
        sleepThread(50);

        while (!mc.player.verticalCollision) {
            thread.checkThreadStopped();
            sleepThread(50);
            Thread.yield();
        }
    }

    @Override
    public void save(JsonObject jsonObject) {}

    @Override
    public void load(JsonObject jsonObject) {
        ActionBotConfig.tasks.add(new JumpTask());
    }
}
