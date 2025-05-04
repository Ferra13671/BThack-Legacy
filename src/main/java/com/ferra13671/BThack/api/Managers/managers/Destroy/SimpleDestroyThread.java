package com.ferra13671.BThack.api.Managers.managers.Destroy;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import net.minecraft.util.math.BlockPos;

public class SimpleDestroyThread extends AbstractDestroyThread {
    BlockPos pos;

    public SimpleDestroyThread(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    protected void destroyAction() throws ThreadClosedException {
        if (mc.player == null || mc.world == null) {
            DestroyManager.isDestroying = false;
            return;
        }

        DestroyManager.isDestroying = true;

        if (ModuleList.packetMine.isEnabled()) {
            pc.startBlockBreaking(pos, RotateUtils.getInvertedFacingEntity(mc.player));
            Thread.yield();
        }

        if (BlockUtils.canBreak(pos)) {
            checkThreadStopped();
            if (ModuleList.packetMine.isEnabled()) {
                ModuleList.packetMine.updateBlock(pos);
                Thread.yield();
            }

            while (BlockUtils.canBreak(pos)) {
                checkThreadStopped();
                if (ModuleList.packetMine.isEnabled()) {
                    if (!BuildManager.isPossibleRich(pos)) break;
                    if (ModuleList.packetMine.currentBreakingBlock == null)
                        ModuleList.packetMine.updateBlock(pos);
                    sleepThread(50);
                } else {
                    if (BuildManager.isPossibleRich(pos)) {
                        AutoTool.equipBestSlot(mc.world.getBlockState(pos));

                        DestroyManager.currentBlockPos = pos;

                        sleepThread(50);
                    } else {
                        break;
                    }
                }
            }
        }
    }
}
