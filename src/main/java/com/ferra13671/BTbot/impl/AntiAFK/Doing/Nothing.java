package com.ferra13671.BTbot.impl.AntiAFK.Doing;

import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFKThread;
import com.ferra13671.BThack.api.Managers.managers.Thread.BThackThread;

public class Nothing extends BThackThread {

    @Override
    public void threadAction() {
        if (StartAntiAFKThread.startDoing) return;
        StartAntiAFKThread.startDoing = true;
        double d = StartAntiAFK.delay * 1000;
        long delay = (long) d;
        sleepThread(delay);
        StartAntiAFKThread.startDoing = false;
        new StartAntiAFKThread().start();
    }
}
