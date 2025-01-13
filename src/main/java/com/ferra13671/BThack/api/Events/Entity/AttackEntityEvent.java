package com.ferra13671.BThack.api.Events.Entity;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class AttackEntityEvent extends Event {
    private final PlayerEntity player;
    private final Entity entity;

    public AttackEntityEvent(PlayerEntity player, Entity entity) {
        this.player = player;
        this.entity = entity;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
