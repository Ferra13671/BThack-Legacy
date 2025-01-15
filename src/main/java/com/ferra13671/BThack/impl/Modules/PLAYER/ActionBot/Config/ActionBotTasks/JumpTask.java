package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;


import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;

import java.util.Arrays;

public class JumpTask extends ActionBotTask {

    public JumpTask() {
        super("Jump");
        this.mode = "Jump";

        this.taskDescription = Arrays.asList(
                "When the task is activated, the player starts jumping."
        );

    }


    @Override
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
    public void save(JsonObject jsonObject) {
        //No action
    }

    @Override
    public void load(JsonObject jsonObject) {
        ActionBotConfig.tasks.add(new JumpTask());
    }
}
