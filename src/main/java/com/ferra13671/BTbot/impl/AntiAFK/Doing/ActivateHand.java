package com.ferra13671.BTbot.impl.AntiAFK.Doing;

import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFKThread;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import net.minecraft.util.Hand;

public class ActivateHand extends BThackThread implements Mc {

    @Override
    public void threadAction() {
        if (StartAntiAFKThread.startDoing) return;
        StartAntiAFKThread.startDoing = true;
        ItemUtils.useItem(Hand.MAIN_HAND, true, mc.player.getYaw(), mc.player.getPitch());
        double d = StartAntiAFK.delay * 1000;
        long delay = (long) d;
        sleepThread(delay);
        StartAntiAFKThread.startDoing = false;
        new StartAntiAFKThread().start();
    }
}
