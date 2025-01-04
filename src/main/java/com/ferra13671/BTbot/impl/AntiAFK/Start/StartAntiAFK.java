package com.ferra13671.BTbot.impl.AntiAFK.Start;

import com.ferra13671.BTbot.impl.AntiAFK.Check.NullCheck;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class StartAntiAFK implements Mc {



    public static double radiusCenterX;
    public static double radiusCenterZ;
    public static boolean active = false;
    public static int messageSize = 5;
    public static double delay = 2.0;
    public static double walkRadius = 4;



    public static void start() {
        active = true;

        if (NullCheck.nullCheck()) return;

        radiusCenterX = mc.player.getX();
        radiusCenterZ = mc.player.getZ();

        new StartAntiAFKThread().start();

        ChatUtils.sendMessage("AntiAFK " + Formatting.GREEN + "enabled" + Formatting.RESET + ".");
    }

    public static void stop() {
        active = false;

        if (NullCheck.nullCheck()) return;

        ChatUtils.sendMessage("AntiAFK " + Formatting.RED + "disabled" + Formatting.RESET + ". BTBot was happy to help you :3");
    }
}
