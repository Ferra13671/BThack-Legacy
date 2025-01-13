package com.ferra13671.BThack.api.Events.Block;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class UseBlockEvent extends Event {
    private final PlayerEntity player;
    private final World world;
    private final Hand hand;
    public BlockHitResult blockHitResult;

    public UseBlockEvent(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
        this.player = player;
        this.world = world;
        this.hand = hand;
        this.blockHitResult = blockHitResult;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public World getWorld() {
        return this.world;
    }

    public Hand getHand() {
        return this.hand;
    }

    public BlockHitResult getBlockHitResult() {
        return this.blockHitResult;
    }
}
