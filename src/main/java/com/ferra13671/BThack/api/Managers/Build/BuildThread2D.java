package com.ferra13671.BThack.api.Managers.Build;

import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

// TODO: Still need to test it as there are many flaws.
public class BuildThread2D extends AbstractBuildThread {
    private int delayTicks;
    private ArrayList<Vec2f> buildSchematic2D;

    private BlockPos startPos;
    private int direction;
    private List<Block> blocks;

    public void set2DSchematic(int delayTicks, ArrayList<Vec2f> buildSchematic2D, BlockPos startPos, int direction) {
        this.delayTicks = delayTicks;
        this.buildSchematic2D = buildSchematic2D;
        this.startPos = startPos;
        this.direction = direction;
    }

    public void setNeedBlocks(List<Block> blocks) {
        if (blocks != null) {
            this.blocks = blocks;
            return;
        }
        this.blocks = new ArrayList<>();
    }

    @Override
    protected void buildAction() {
        if (mc.player == null || mc.world == null) return;

        BuildManager.isBuilding = true;

        ArrayList<BlockPos> positions = getBlockPositions();

        while (BuildManager.check(positions)) {
            for (BlockPos pos : positions) {
                if (!BuildManager.pickUpPlaceBlocks(true, blocks)) {
                    return;
                }
                if (BuildManager.isPossibleRich(pos)) {
                    Block block = mc.world.getBlockState(pos).getBlock();

                    if (BuildManager.ignoreBlocks.contains(block)) {

                        BuildManager.placeBlock(pos);
                        BuildManager.delay((long) (delayTicks * mc.renderTickCounter.tickTime) - 15, pos, this);
                    }
                } else {
                    return;
                }
            }
        }
    }

    private ArrayList<BlockPos> getBlockPositions() {
        ArrayList<BlockPos> positions = new ArrayList<>();

        for (Vec2f vector : buildSchematic2D) {
            switch (direction) {
                case 90:
                    positions.add(new ModifyBlockPos(new Vec3d(startPos.getX(), startPos.getY() + vector.y, startPos.getZ() - vector.x)));
                    break;
                case 180:
                    positions.add(new ModifyBlockPos(new Vec3d(startPos.getX() + vector.x, startPos.getY() + vector.y, startPos.getZ())));
                    break;
                case 270:
                    positions.add(new ModifyBlockPos(new Vec3d(startPos.getX(), startPos.getY() + vector.y, startPos.getZ() + vector.x)));
                    break;
                case 0:
                default:
                    positions.add(new ModifyBlockPos(new Vec3d(startPos.getX() - vector.x, startPos.getY() + vector.y, startPos.getZ())));
            }
        }
        return positions;
    }
}
