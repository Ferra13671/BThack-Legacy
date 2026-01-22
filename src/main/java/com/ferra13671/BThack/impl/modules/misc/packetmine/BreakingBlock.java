package com.ferra13671.BThack.impl.modules.misc.packetmine;

import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.api.utils.BlockUtils;
import net.minecraft.util.math.BlockPos;

public class BreakingBlock implements Mc {
    public final BlockPos blockPos;
    public double currentDestroyProgress;
    public double prevDestroyProgress;
    public boolean startDestroying = false;

    public BreakingBlock(BlockPos blockPos) {
        this.blockPos = blockPos;
        currentDestroyProgress = 0;
        prevDestroyProgress = 0;
    }

    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "DataFlowIssue"})
    public boolean canBreak() {
        return !mc.world.isAir(blockPos) && BlockUtils.canBreak(blockPos);
    }
}
