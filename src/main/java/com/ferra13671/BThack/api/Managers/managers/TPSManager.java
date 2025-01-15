package com.ferra13671.BThack.api.Managers.managers;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.Manager;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.math.MathHelper;

public class TPSManager implements Manager {
    private long prevTime;
    private final float[] ticks = new float[20];
    private int currentTick;

    public TPSManager() {
        prevTime = -1;

        for (int i = 0, len = ticks.length; i < len; i++) {
            ticks[i] = 0.0f;
        }
    }

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
        BThack.debug("TPS Manager inited.");
    }

    public float getTickRate() {
        int tickCount = 0;
        float tickRate = 0.0f;

        for (final float tick : ticks) {
            if (tick > 0.0f) {
                tickRate += tick;
                tickCount++;
            }
        }

        return MathHelper.clamp((tickRate / tickCount), 0.0f, 20.0f);
    }

    @EventSubscriber
    public void onPacket(PacketEvent.Receive e) {
        if (e.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            if (prevTime != -1) {
                ticks[currentTick % ticks.length] = MathHelper.clamp((20.0f / ((float) (System.currentTimeMillis() - prevTime) / 1000.0f)), 0.0f, 20.0f);
                currentTick++;
            }

            prevTime = System.currentTimeMillis();
        }
    }
}
