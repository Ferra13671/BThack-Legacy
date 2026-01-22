package com.ferra13671.BThack.managers.impl;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.events.entity.FireworkTickEvent;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.api.utils.Initializable;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.Ticker;
import com.ferra13671.BThack.mixins.accessor.entity.IFireworkRocketEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;

public class FireworkManager implements Initializable, Mc {
    private final Ticker lastClientUseFireworkTicker = new Ticker();
    public long lastFireWorkTick = 0;

    private FireworkRocketEntity firework;

    private int delayTicks = 0;

    public FireworkManager() {
        lastClientUseFireworkTicker.reset();
    }

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
        BThack.debug("Firework Manager inited!");
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onFireworkTick(FireworkTickEvent e) {
        if (mc.player.isGliding() && firework != e.firework
                && ((IFireworkRocketEntity) e.firework).hookWasShotByEntity()
                && ((IFireworkRocketEntity) e.firework).getShooter() == mc.player) {
            firework = e.firework;
        }
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (Module.nullCheck()) {
            if (firework != null) {
                firework = null;
            }
        }

        if (firework == null) return;

        delayTicks++;
        if (delayTicks > 10) {
            delayTicks = 0;
            for (Entity entity : mc.world.getEntities()) {
                if (entity == firework) {
                    return;
                }
            }
            firework = null;
        }
    }

    public void updateFireWorkTick() {
        lastFireWorkTick = System.currentTimeMillis();
    }

    public boolean isUsingFireWork() {
        if (Module.nullCheck()) {
            firework = null;
            return false;
        }
        if (!lastClientUseFireworkTicker.passed(500)) return true;
        return firework != null && !firework.isRemoved();
    }

    public void onExplode(FireworkRocketEntity firework) {
        if (firework == this.firework)
            this.firework = null;
    }

    public void resetLastClientUseFireworkTicker() {
        lastClientUseFireworkTicker.reset();
    }
}
