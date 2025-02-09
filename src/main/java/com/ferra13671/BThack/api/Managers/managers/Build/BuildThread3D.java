package com.ferra13671.BThack.api.Managers.managers.Build;

import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.mixins.accessor.IRenderTickCounter$Dynamic;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuildThread3D extends AbstractBuildThread {
    private ArrayList<Vec3d> buildSchematic3D;


    public void set3DSchematic(int delayTicks, ArrayList<Vec3d> buildSchematic3D, BlockPos startPos) {
        this.delayTicks = delayTicks;
        this.buildSchematic3D = buildSchematic3D;
        this.startPos = startPos;
    }

    public void set3DSchematic(int delayTicks, List<Vec3i> buildSchematic3D, BlockPos startPos) {
        this.delayTicks = delayTicks;
        ArrayList<Vec3d> sch = new ArrayList<>();
        for (Vec3i vec3i : buildSchematic3D) {
            sch.add(new Vec3d(vec3i.getX(), vec3i.getY(), vec3i.getZ()));
        }
        this.buildSchematic3D = sch;
        this.startPos = startPos;
    }

    public void setNeedBlocks(List<Block> blocks) {
        this.blocks = Objects.requireNonNullElseGet(blocks, ArrayList::new);
    }

    @Override
    protected void buildAction() throws ThreadClosedException {
        if (mc.player == null || mc.world == null) return;

        BuildManager.isBuilding = true;

        ArrayList<BlockPos> positions = new ArrayList<>();

        for (Vec3d vector : buildSchematic3D) {
            positions.add(BlockPos.ofFloored(new Vec3d(startPos.getX() + vector.x, startPos.getY() + vector.y, startPos.getZ() + vector.z)));
        }

        for (int i = 0; i < 5; i++) {
            checkThreadStopped();
            for (BlockPos pos : positions) {
                checkThreadStopped();
                if (!BuildManager.pickUpPlaceBlocks(true, blocks) && !blocks.isEmpty()) {
                    return;
                }
                if (BuildManager.isPossibleRich(pos)) {
                    Block block = mc.world.getBlockState(pos).getBlock();

                    if (BuildManager.ignoreBlocks.contains(block)) {
                        FacingBlock fBlock = BuildManager.checkNearBlocksExtended(pos);
                        if (fBlock == null) {
                            continue;
                        }

                        Managers.BUILD_MANAGER.blockPoses.add(pos);
                        sleepThread((long) (delayTicks == 0 ? 1 : (delayTicks * ((IRenderTickCounter$Dynamic)  mc.getRenderTickCounter()).getTickTime())));
                    }
                } else {
                    return;
                }
            }
        }
    }
}