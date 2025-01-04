package com.ferra13671.BThack.api.Managers.Build;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Managers.Thread.ThreadClosedException;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBuildThread extends BThackThread implements Mc {

    protected BlockPos startPos;
    protected int delayTicks;
    protected List<Block> blocks = new ArrayList<>();

    @Override
    public void threadAction() throws ThreadClosedException {
        try {
            buildAction();
        } finally {
            if (BuildManager.isBuilding)
                BuildManager.isBuilding = false;
        }
    }

    protected abstract void buildAction() throws ThreadClosedException;
}
