package com.ferra13671.BTbot.impl.AntiAFK.Doing.GotoN;

import com.ferra13671.BTbot.api.Utils.Generate.GenerateNumber;
import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFKThread;

public class PrepareGotoN {
    public double newX;
    public double newZ;

    public PrepareGotoN() {
    }

    public void prepare() {
        if (StartAntiAFKThread.startDoing) return;
        StartAntiAFKThread.startDoing = true;
        double r = StartAntiAFK.walkRadius;
        int radius = (int) r;
        int a = GenerateNumber.generateInt(1, radius);
        int b = GenerateNumber.generateInt(1, radius);
        newX = a;
        newZ = b;
        boolean negative = GenerateNumber.generateInt(0, 1) != 0;

        if (!negative) {
            newX = StartAntiAFK.radiusCenterX + newX;
            newZ = StartAntiAFK.radiusCenterZ + newZ;
        } else {
            newX = StartAntiAFK.radiusCenterX - newX;
            newZ = StartAntiAFK.radiusCenterZ - newZ;
        }
    }
}
