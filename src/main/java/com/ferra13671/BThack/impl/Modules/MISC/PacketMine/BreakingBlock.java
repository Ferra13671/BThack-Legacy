package com.ferra13671.BThack.impl.Modules.MISC.PacketMine;

import net.minecraft.util.math.BlockPos;

public class BreakingBlock {

    public final BlockPos blockPos;
    public double currentDestroyProgress;
    public boolean startDestroying = false;

    public BreakingBlock(BlockPos blockPos) {
        this.blockPos = blockPos;
        currentDestroyProgress = 0;
    }
}
