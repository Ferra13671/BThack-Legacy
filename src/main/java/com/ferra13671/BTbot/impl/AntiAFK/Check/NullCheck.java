package com.ferra13671.BTbot.impl.AntiAFK.Check;

import com.ferra13671.BThack.api.Interfaces.Mc;

public class NullCheck implements Mc {
    public static boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }
}
