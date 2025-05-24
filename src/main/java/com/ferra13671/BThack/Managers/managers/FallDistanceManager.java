package com.ferra13671.BThack.managers.managers;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Utils.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class FallDistanceManager implements Initializable, Mc {
    private double fallDistance = 0;

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (!Module.nullCheck() && !mc.player.isOnGround()) {
            double yDelta = mc.player.getY() - mc.player.prevY;
            if (yDelta < 0)
                fallDistance -= yDelta;
            else fallDistance = 0;
        } else  fallDistance = 0;
    }

    public double getFallDistance() {
        return fallDistance;
    }
}
