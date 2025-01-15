package com.ferra13671.BThack.api.Managers.managers.Destroy;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class DestroyThread3D extends AbstractDestroyThread {
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
            //PaskalABC and Java lore:
            ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

            this.ignoreBlocks = ignoreBlocks;
            return;
        }
        this.ignoreBlocks = new ArrayList<>();
    }

    @Override
    protected void destroyAction() throws ThreadClosedException {
        if (Module.nullCheck() || schematic == null) {
            return;
        }
        DestroyManager.currentBlockPos = null;

        DestroyManager.isDestroying = true;

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
                        if (!BuildManager.isPossibleRich(pos)) break;
                        if (ModuleList.packetMine.currentBreakingBlock == null)
                            ModuleList.packetMine.updateBlock(pos);
                        DestroyManager.delay(50, this);
                    } else {
                        if (BuildManager.isPossibleRich(pos)) {
                            //mc.player.yaw = AimBotUtils.rotations(pos)[0];
                            //mc.player.pitch = AimBotUtils.rotations(pos)[1];
                            AutoTool.equipBestSlot(mc.world.getBlockState(pos));

                            DestroyManager.currentBlockPos = pos;

                            DestroyManager.delay(50, this);
                        } else {
                            break;
                        }
                    }
                }
            }
            DestroyManager.currentBlockPos = null;
            mc.interactionManager.cancelBlockBreaking();
        }
    }
}
