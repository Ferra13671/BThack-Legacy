package com.ferra13671.BThack.api.Events.Block;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AttackBlockEvent extends Event {
    private final PlayerEntity player;
    private final BlockPos blockPos;
    private final Direction direction;

    public AttackBlockEvent(PlayerEntity player, BlockPos blockPos, Direction direction) {
        this.player = player;
        this.blockPos = blockPos;
        this.direction = direction;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public Direction getDirection() {
        return this.direction;
    }
}
