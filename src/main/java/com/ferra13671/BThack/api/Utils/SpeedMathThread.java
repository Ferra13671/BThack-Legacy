package com.ferra13671.BThack.api.Utils;


import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Module.Module;

public final class SpeedMathThread extends BThackThread implements Mc {

    public static double speed = 0;
    public static boolean active = false;

    @Override
    public void threadAction() {
        active = true;
        while (ModuleList.HUD.isEnabled()) {
            if (!Module.nullCheck()) {
                double oldX = mc.player.getX();
                double oldZ = mc.player.getZ();

                sleepThread(200);

                if (!Module.nullCheck()) {

                    double newX = mc.player.getX();
                    double newZ = mc.player.getZ();

                    double sX = Math.pow(newX - oldX, 2);
                    double sZ = Math.pow(newZ - oldZ, 2);

                    speed = (Math.sqrt(sX + sZ)) * 5;
                }
            } else {
                break;
            }
        }
        active = false;
        speed = 0;
    }
}
