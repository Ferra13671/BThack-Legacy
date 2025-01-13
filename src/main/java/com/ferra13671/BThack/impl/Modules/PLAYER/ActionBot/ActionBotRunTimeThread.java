package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import net.minecraft.util.Formatting;

public class ActionBotRunTimeThread extends BThackThread {
    public static boolean isPlaying = false;

    @Override
    public void threadAction() throws ThreadClosedException {
        ChatUtils.sendMessage("[ActionBot] "  + Formatting.AQUA + "Starting to reproduce the task chain.");

        if (ModuleList.actionBot.repeat.getValue()) {
            while (ModuleList.actionBot.isEnabled() && ModuleList.actionBot.repeat.getValue()) {
                checkThreadStopped();
                play();
            }

            ModuleList.actionBot.setToggled(false);
        } else {
            play();

            ModuleList.actionBot.setToggled(false);
        }
    }

    public void play() throws ThreadClosedException {
        isPlaying = true;

        for (ActionBotTask task : ActionBotConfig.tasks) {
            checkThreadStopped();
            if (!task.isStartOrEndTask()) {
                task.thread = this;

                task.play();
            }
        }

        isPlaying = false;
    }
}
