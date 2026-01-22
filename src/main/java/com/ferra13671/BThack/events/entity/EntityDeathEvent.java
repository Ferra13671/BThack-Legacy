package com.ferra13671.BThack.events.entity;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.Entity;

public class EntityDeathEvent extends Event {
    public final Entity entity;

    public EntityDeathEvent(Entity entity) {
        this.entity = entity;
    }
}
