package com.ferra13671.BTbot.impl.AntiAFK.Doing;

import com.ferra13671.BTbot.api.Utils.Generate.NumberGenerator;
import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFKThread;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Utils.ChatUtils;

public class SendToChat extends BThackThread implements Mc {
    public static long number = 0;

    @Override
    public void threadAction() {
        if (StartAntiAFKThread.startDoing) return;
        StartAntiAFKThread.startDoing = true;
        int s = StartAntiAFK.messageSize;
        for (int i = 0; i < s; i++) {
            if (number == 0) {
                number = NumberGenerator.generateInt(0,9);
            } else {
                number = (number * 10) + NumberGenerator.generateInt(0,9);
            }
        }
        ChatUtils.sendChatMessage(String.valueOf(number));
        double d = StartAntiAFK.delay * 1000;
        long delay = (long) d;
        sleepThread(delay);
        StartAntiAFKThread.startDoing = false;
        new StartAntiAFKThread().start();
    }
}
