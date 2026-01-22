package com.ferra13671.BThack.managers.impl.Break;

import com.ferra13671.BThack.managers.impl.thread.ThreadClosedException;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.managers.impl.place.PlaceManager;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.BlockUtils;
import com.ferra13671.BThack.impl.modules.player.AutoTool;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class BreakThread3D extends AbstractBreakThread {
    private ArrayList<Vec3d> schematic;

    public void set3DSchematic(ArrayList<Vec3d> sch, BlockPos startPos) {
        schematic = sch;
        this.startPos = startPos;
    }

    public void set3DSchematic(List<Vec3i> sch, BlockPos startPos) {
        ArrayList<Vec3d> _sch = new ArrayList<>();
        for (Vec3i vec3i : sch) {
            _sch.add(new Vec3d(vec3i.getX(), vec3i.getY(), vec3i.getZ()));
        }

        schematic = _sch;
        this.startPos = startPos;
    }

    public void setIgnoreBlocks(List<Block> ignoreBlocks) {
        if (ignoreBlocks != null) {
            this.ignoreBlocks = ignoreBlocks;
            return;
        }
        this.ignoreBlocks = new ArrayList<>();
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    protected void destroyAction() throws ThreadClosedException {
        if (Module.nullCheck() || schematic == null) {
            return;
        }
        BreakManager.currentBlockPos = null;

        BreakManager.isDestroying = true;

        ArrayList<BlockPos> sch = new ArrayList<>();

        for (Vec3d vector : schematic) {
            sch.add(BlockPos.ofFloored(new Vec3d(startPos.getX() + vector.x, startPos.getY() + vector.y, startPos.getZ() + vector.z)));
        }

        for (BlockPos pos : sch) {
            checkThreadStopped();
            if (ignoreBlocks.contains(mc.world.getBlockState(pos).getBlock()))
                continue;
            if (BlockUtils.canBreak(pos)) {
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
            BreakManager.currentBlockPos = null;
            mc.interactionManager.cancelBlockBreaking();
        }
    }
}
