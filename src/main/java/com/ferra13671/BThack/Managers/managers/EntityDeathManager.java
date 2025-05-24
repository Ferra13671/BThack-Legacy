package com.ferra13671.BThack.managers.managers;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.events.Entity.EntityDeathEvent;
import com.ferra13671.BThack.api.Utils.Mc;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.LivingEntity;

public class EntityDeathManager implements Initializable, Mc {

    public EntityDeathManager() {
        delayTicker.reset();
    }

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
        BThack.debug("Entity Death Manager inited!");
    }

    Ticker delayTicker = new Ticker();
    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
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
