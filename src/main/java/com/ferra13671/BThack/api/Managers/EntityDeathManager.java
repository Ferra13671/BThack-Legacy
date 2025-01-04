package com.ferra13671.BThack.api.Managers;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.Entity.EntityDeathEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.LivingEntity;

public class EntityDeathManager implements Mc {

    public EntityDeathManager() {
        delayTicker.reset();
    }


    Ticker delayTicker = new Ticker();
    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (Module.nullCheck()) return;

        if (delayTicker.passed(500)) {
            mc.world.getEntities().forEach(entity -> {
                if (entity instanceof LivingEntity entity2 && entity2.isDead()) {
                    EntityDeathEvent event = new EntityDeathEvent(entity2);
                    BThack.EVENT_BUS.activate(event);
                }
            });
            delayTicker.reset();
        }
    }
}
