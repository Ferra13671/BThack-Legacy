package com.ferra13671.BThack.api.Managers;

import com.ferra13671.BThack.api.Events.Entity.FireworkTickEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.mixins.accessor.IFireworkRocketEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;

public class FireWorkManager implements Mc {
    private final Ticker lastClientUseFireworkTicker = new Ticker();
    public long lastFireWorkTick = 0;

    private FireworkRocketEntity firework;

    private int delayTicks = 0;

    public FireWorkManager() {
        lastClientUseFireworkTicker.reset();
    }

    @EventSubscriber
    public void onFireworkTick(FireworkTickEvent e) {
        if (mc.player.isFallFlying() && firework != e.firework
                && ((IFireworkRocketEntity) e.firework).hookWasShotByEntity()
                && ((IFireworkRocketEntity) e.firework).getShooter() == mc.player) {
            firework = e.firework;
        }
    }

    @EventSubscriber
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

    /*
    public boolean timePassedFromLastFireWorkUse(int millis) {
        return (System.currentTimeMillis() - lastFireWorkTick) > (mc.renderTickCounter.tickTime + 80 + millis);
    }

     */

    public void onExplode(FireworkRocketEntity firework) {
        if (firework == this.firework)
            this.firework = null;
    }

    public void resetLastClientUseFireworkTicker() {
        lastClientUseFireworkTicker.reset();
    }
}
