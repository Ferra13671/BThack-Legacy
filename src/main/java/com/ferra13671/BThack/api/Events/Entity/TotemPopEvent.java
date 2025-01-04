package com.ferra13671.BThack.api.Events.Entity;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.Entity;

public class TotemPopEvent extends Event {
    public final Entity entity;
    public final int totemsPopped;

    public TotemPopEvent(Entity entity, int totemsPopped) {
        this.entity = entity;
        this.totemsPopped = totemsPopped;
    }
}
