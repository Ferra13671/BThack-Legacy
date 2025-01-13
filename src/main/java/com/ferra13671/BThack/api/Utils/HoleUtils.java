package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class HoleUtils implements Mc {
    public static BlockPos[] holeOffsets = new BlockPos[] {
            new BlockPos(1, 0, 0),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(0, 0, -1),
            new BlockPos(0, -1, 0)
    };


    public static boolean isObsidianHole(BlockPos blockPos) {
        boolean isObsidianHole = true;

        for (BlockPos blockPos1 : holeOffsets) {
            Block block = mc.world.getBlockState(blockPos.add(blockPos1)).getBlock();

            if (block != Blocks.OBSIDIAN) {
                isObsidianHole = false;
            }
        }

        if (mc.world.getBlockState(blockPos.add(0, 0, 0)).getBlock() != Blocks.AIR || mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() != Blocks.AIR || mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() != Blocks.AIR) {
            isObsidianHole = false;
        }

        return isObsidianHole;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        boolean isBedrockHole = true;

        for (BlockPos blockPos1 : holeOffsets) {
            Block block = mc.world.getBlockState(blockPos.add(blockPos1)).getBlock();

            if (block != Blocks.BEDROCK) {
                isBedrockHole = false;
            }
        }

        if (mc.world.getBlockState(blockPos.add(0, 0, 0)).getBlock() != Blocks.AIR || mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() != Blocks.AIR || mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() != Blocks.AIR) {
            isBedrockHole = false;
        }

        return isBedrockHole;
    }

    public static boolean isMutableHole(BlockPos blockPos, boolean falseIfBedrockHole) {
        if (falseIfBedrockHole)
            if (isBedrockHole(blockPos))
                return false;
        boolean isMutrableHole = true;

        for (BlockPos blockPos1 : holeOffsets) {
            Block block = mc.world.getBlockState(blockPos.add(blockPos1)).getBlock();

            if (block != Blocks.OBSIDIAN && block != Blocks.BEDROCK) {
                isMutrableHole = false;
            }
        }

        if (!ignoreBlocks.contains(mc.world.getBlockState(blockPos.add(0, 0, 0)).getBlock()) || !ignoreBlocks.contains(mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock()) || !ignoreBlocks.contains(mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock())) {
            isMutrableHole = false;
        }

        return isMutrableHole;
    }

    public static boolean isPlayerInHole(PlayerEntity entityPlayer) {
        Vec3d[] hole = {
                entityPlayer.getPos().add(1.0, 0.0, 0.0),
                entityPlayer.getPos().add(-1.0, 0.0, 0.0),
                entityPlayer.getPos().add(0.0, 0.0, 1.0),
                entityPlayer.getPos().add(0.0, 0.0, -1.0),
                entityPlayer.getPos().add(0.0, -1.0, 0.0)
        };

        int holeBlocks = 0;

        for (Vec3d vec3d : hole) {
            BlockPos offset = new ModifyBlockPos(vec3d.x, vec3d.y, vec3d.z);

            if (mc.world.getBlockState(offset).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(offset).getBlock() == Blocks.BEDROCK) {
                holeBlocks++;
            }

            if (holeBlocks == 5) {
                return true;
            }
        }

        return false;
    }

    private static final Set<Block> ignoreBlocks = new HashSet<>(Arrays.asList(Blocks.AIR, Blocks.CAVE_AIR, Blocks.VOID_AIR));
}
