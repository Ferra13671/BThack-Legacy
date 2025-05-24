package com.ferra13671.BThack.managers.managers.Break;

import com.ferra13671.BThack.managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Utils.Mc;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBreakThread extends BThackThread implements Mc {

    protected BlockPos startPos;
    protected List<Block> ignoreBlocks = new ArrayList<>();

    @Override
    public void threadAction() throws ThreadClosedException {
        try {
            destroyAction();
        } finally {
            reset();
        }
    }

    public void reset() {
        BreakManager.currentBlockPos = null;
        BreakManager.isDestroying = false;
        if (mc.interactionManager != null)
            mc.interactionManager.cancelBlockBreaking();
    }

    protected abstract void destroyAction() throws ThreadClosedException;
}
