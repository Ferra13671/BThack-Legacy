package com.ferra13671.BThack.managers.managers.Place;

import com.ferra13671.BThack.managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Utils.Mc;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlaceThread extends BThackThread implements Mc {

    protected BlockPos startPos;
    protected int delayTicks;
    protected List<Block> blocks = new ArrayList<>();

    @Override
    public void threadAction() throws ThreadClosedException {
        try {
            buildAction();
        } finally {
            if (PlaceManager.isBuilding)
                PlaceManager.isBuilding = false;
        }
    }

    protected abstract void buildAction() throws ThreadClosedException;
}
