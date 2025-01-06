package com.ferra13671.BThack.api.Managers.Build;

import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBuildThread extends Thread implements Mc {

    protected BlockPos startPos;
    protected int delayTicks;
    protected List<Block> blocks = new ArrayList<>();

    @Override
    public final void run() {
        super.run();
        try {
            buildAction();
        } finally {
            if (BuildManager.isBuilding)
                BuildManager.isBuilding = false;
        }
    }

    protected abstract void buildAction();
}
