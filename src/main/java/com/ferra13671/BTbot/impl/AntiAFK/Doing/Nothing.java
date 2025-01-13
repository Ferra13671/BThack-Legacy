package com.ferra13671.BTbot.impl.AntiAFK.Doing;

import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFKThread;

public class Nothing extends Thread{
    public void run() {
        if (StartAntiAFKThread.startDoing) return;
        StartAntiAFKThread.startDoing = true;
        double d = StartAntiAFK.delay * 1000;
        long delay = (long) d;
        try {
            sleep(delay);
        } catch (InterruptedException ignored) {}
        StartAntiAFKThread.startDoing = false;
        new StartAntiAFKThread().start();
    }
}
