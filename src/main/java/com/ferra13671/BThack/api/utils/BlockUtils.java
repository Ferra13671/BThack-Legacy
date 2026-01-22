package com.ferra13671.BThack.api.utils;

import com.ferra13671.BThack.api.utils.rotate.RotateUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.List;

public final class BlockUtils implements Mc {

    @SuppressWarnings("DataFlowIssue")
    public static BlockState getState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static int getId(BlockPos pos) {
        return Block.getRawIdFromState(getState(pos));
    }

    public static Block getBlockFromNameOrID(String nameOrId) {
        if (MathUtils.isInteger(nameOrId)) {
            BlockState state = Block.STATE_IDS.get(Integer.parseInt(nameOrId));
            if (state == null)
                return null;

            return state.getBlock();
        }

        try {
            Block block = Registries.BLOCK.getOptionalValue(Identifier.of(nameOrId))
                    .orElse(null);
            if (block == null)
                block = Registries.BLOCK.getOptionalValue(Identifier.of("minecraft:" + nameOrId)).orElse(null);

            return block;
        } catch (InvalidIdentifierException e) {
            return null;
        }
    }

    public static String getBlockName(Block block) {
        return block.getTranslationKey().replace("block.minecraft.", "");
    }

    private static VoxelShape getOutlineShape(BlockPos pos) {
        return getState(pos).getOutlineShape(mc.world, pos);
    }

    public static Box getBoundingBox(BlockPos pos) {
        VoxelShape shape = getOutlineShape(pos);
        return !shape.isEmpty() ? shape.getBoundingBox().offset(pos) : createBox(pos, 0.5, 0.5, 1, false);
    }

    public static boolean canBeClicked(BlockPos pos) {
        return getOutlineShape(pos) != VoxelShapes.empty();
    }

    public static boolean isOpaqueFullCube(BlockPos pos) {
        return getState(pos).isOpaqueFullCube();
    }

    @SuppressWarnings("DataFlowIssue")
    public static BlockHitResult raycast(Vec3d from, Vec3d to, RaycastContext.FluidHandling fluidHandling) {
        return mc.world.raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, fluidHandling, mc.player));
    }

    public static BlockHitResult raycast(Vec3d from, Vec3d to) {
        return raycast(from, to, RaycastContext.FluidHandling.NONE);
    }

    public static boolean hasLineOfSight(Vec3d from, Vec3d to) {
        return raycast(from, to).getType() == HitResult.Type.MISS;
    }

    public static boolean hasLineOfSight(Vec3d to) {
        return raycast(RotateUtils.getEyesPos(), to)
                .getType() == HitResult.Type.MISS;
    }

    public static List<BlockPos> getNearbyBlocks(PlayerEntity entityPlayer, double blockRange, boolean motion) {
        List<BlockPos> nearbyBlocks = new ArrayList<>();

        int range = (int) MathUtils.roundNumber(blockRange, 0);

        if (motion)
            entityPlayer.getPos().add(new Vec3d(entityPlayer.velocity.x, entityPlayer.velocity.y, entityPlayer.velocity.z));

        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    nearbyBlocks.add(entityPlayer.getBlockPos().add(x, y, z));

        return nearbyBlocks;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float radius, float height, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleBlocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) radius; x <= cx + radius; x++) {
            for (int z = cz - (int) radius; z <= cz + radius; z++) {
                for (int y = (sphere ? cy - (int) height : cy); y < (cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))) {
                        circleBlocks.add(new BlockPos(x, y + plus_y, z));
                    }
                }
            }
        }
        return circleBlocks;
    }

    public static Box getBox(BlockEntity be) {
        BlockPos pos = be.getPos();

        if(!canBeClicked(pos))
            return null;

        if(be instanceof ChestBlockEntity)
            return getChestBox(be);
        else if (be instanceof EnderChestBlockEntity)
            return getEnderChestBox(be);

        return getBoundingBox(pos);
    }

    private static Box getChestBox(BlockEntity chestBE) {
        BlockState state = chestBE.getCachedState();
        if(!state.contains(ChestBlock.CHEST_TYPE))
            return null;

        ChestType chestType = state.get(ChestBlock.CHEST_TYPE);

        BlockPos pos = chestBE.getPos();
        Box box = getBoundingBox(pos);

        if(chestType != ChestType.SINGLE) {
            BlockPos pos2 = pos.offset(ChestBlock.getFacing(state));

            if(canBeClicked(pos2)) {
                Box box2 = getBoundingBox(pos2);
                box = box.union(box2);
            }
        }

        return box;
    }

    private static Box getEnderChestBox(BlockEntity chestBE) {

        BlockPos pos = chestBE.getPos();

        return getBoundingBox(pos);
    }

    public static Box createBox(BlockPos blockPos, double length, double width, double height, boolean yOnCenter) {
        Vec3d pos = new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + (yOnCenter ? 0.5 : 0), blockPos.getZ() + 0.5);
        return new Box(pos.x - length, (yOnCenter ? (pos.y - height) : pos.y ), pos.z - width, pos.x + length, pos.y + height, pos.z + width);
    }

    @SuppressWarnings("DataFlowIssue")
    public static boolean canBreak(BlockPos pos, BlockState state) {
        if (!mc.player.isCreative() && state.getHardness(mc.world, pos) < 0) return false;
        if (mc.world.isAir(pos)) return false;
        if (state.getBlock() instanceof FluidBlock) return false;
        return state.getOutlineShape(mc.world, pos) != VoxelShapes.empty();
    }

    @SuppressWarnings("DataFlowIssue")
    public static boolean canBreak(BlockPos pos) {
        return canBreak(pos, mc.world.getBlockState(pos));
    }
}
