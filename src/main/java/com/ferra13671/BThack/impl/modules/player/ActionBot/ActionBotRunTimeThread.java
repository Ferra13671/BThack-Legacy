package com.ferra13671.BThack.impl.modules.player.ActionBot;

import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.managers.impl.thread.BThackThread;
import com.ferra13671.BThack.managers.impl.thread.ThreadClosedException;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTask;
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

            ModuleList.actionBot.setEnabled(false);
        } else {
            play();

            ModuleList.actionBot.setEnabled(false);
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
