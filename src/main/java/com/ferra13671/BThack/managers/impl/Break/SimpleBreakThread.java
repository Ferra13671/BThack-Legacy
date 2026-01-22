package com.ferra13671.BThack.managers.impl.Break;

import com.ferra13671.BThack.managers.impl.thread.ThreadClosedException;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.managers.impl.place.PlaceManager;
import com.ferra13671.BThack.api.utils.BlockUtils;
import com.ferra13671.BThack.api.utils.rotate.RotateUtils;
import com.ferra13671.BThack.impl.modules.player.AutoTool;
import net.minecraft.util.math.BlockPos;

public class SimpleBreakThread extends AbstractBreakThread {
    BlockPos pos;

    public SimpleBreakThread(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    protected void destroyAction() throws ThreadClosedException {
        if (mc.player == null || mc.world == null) {
            BreakManager.isDestroying = false;
            return;
        }

        BreakManager.isDestroying = true;

        if (ModuleList.packetMine.isEnabled()) {
            mc.interactionManager.attackBlock(pos, RotateUtils.getInvertedFacingEntity(mc.player));
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
                    if (!PlaceManager.isPossibleRich(pos)) break;
                    if (ModuleList.packetMine.currentBreakingBlock == null)
                        ModuleList.packetMine.updateBlock(pos);
                    sleepThread(50);
                } else {
                    if (PlaceManager.isPossibleRich(pos)) {
                        AutoTool.equipBestSlot(mc.world.getBlockState(pos));

                        BreakManager.currentBlockPos = pos;

                        sleepThread(50);
                    } else {
                        break;
                    }
                }
            }
        }
    }
}
