package com.ferra13671.BThack.api.Managers.managers.Destroy;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadClosedException;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDestroyThread extends BThackThread implements Mc {

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
        DestroyManager.currentBlockPos = null;
        DestroyManager.isDestroying = false;
        if (mc.interactionManager != null)
            mc.interactionManager.cancelBlockBreaking();
    }

    protected abstract void destroyAction() throws ThreadClosedException;
}
