package com.ferra13671.BThack.events.entity;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.projectile.FireworkRocketEntity;

public class FireworkTickEvent extends Event {
    public FireworkRocketEntity firework;

    public FireworkTickEvent(FireworkRocketEntity firework) {
        this.firework = firework;
    }
}
