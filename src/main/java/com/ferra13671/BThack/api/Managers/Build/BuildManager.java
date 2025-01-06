package com.ferra13671.BThack.api.Managers.Build;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BuildManager implements Mc {

    public static final Set<Block> ignoreBlocks = Sets.newHashSet(
            Blocks.AIR,
            Blocks.CAVE_AIR,
            Blocks.VOID_AIR,
            Blocks.WATER,
            Blocks.WATER_CAULDRON,
            Blocks.LAVA,
            Blocks.LAVA_CAULDRON
    );

    private static final List<Block> shiftBlocks = Arrays.asList(
            Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE,
            Blocks.BIRCH_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.CHERRY_TRAPDOOR,
            Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER,
            Blocks.ACACIA_TRAPDOOR, Blocks.ENCHANTING_TABLE, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX
    );

    public static final List<Block> lavas = Arrays.asList(
            Blocks.LAVA,
            Blocks.LAVA_CAULDRON
    );

    public static boolean isBuilding = false;

    public static void placeBlock(BlockPos pos) {
        RenderSystem.recordRenderCall(() -> {
            try {
                if (!mc.world.getBlockState(pos).isReplaceable()) return;
                BlockState block1 = mc.world.getBlockState(pos);
                if ((!mc.world.isAir(pos) && !block1.isLiquid()) || block1.isSolid()) return;
                FacingBlock block = checkNearBlocksExtended(pos);
                if (block == null) return;
                BlockHitResult bhr;
                bhr = new BlockHitResult(new Vec3d((double) block.pos().getX() + Math.random(), block.pos().getY() + 0.99f, (double) block.pos().getZ() + Math.random()), block.direction(), block.pos(), false);

                float[] rotations = AimBotUtils.rotations(bhr.getPos());
                boolean sneak = BuildManager.needSneak(mc.world.getBlockState(bhr.getBlockPos()).getBlock()) && !mc.player.isSneaking();

                if (sneak)
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));

                GrimUtils.sendPreActionGrimPackets(rotations[0], rotations[1]);
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
                mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
                GrimUtils.sendPostActionGrimPackets();
            } catch (Exception ignored) {}
        });
    }

    public static BlockHitResult getHitResult(BlockPos pos) {
        return getHitResult(pos, true, Direction.UP);
    }

    public static BlockHitResult getHitResult(BlockPos pos, boolean check, Direction direction) {
        FacingBlock block = check ? checkNearBlocksExtended(pos) : new FacingBlock(pos, direction);
        if (block == null) return null;
        return new BlockHitResult(new Vec3d((double) block.pos().getX() + Math.random(), block.pos().getY() + 0.99f, (double) block.pos().getZ() + Math.random()), block.direction(), block.pos(), false);
    }

    protected static boolean check(ArrayList<BlockPos> positions) {
        for (BlockPos pos : positions) {
            FacingBlock fBlock = BuildManager.checkNearBlocksExtended(pos);
            if (fBlock == null) {
                continue;
            }
            if (ignoreBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
                return true;
            }
        }
        return false;
    }

    public static void delay(long milliseconds, BlockPos pos, Thread thread) {
        for (long i = 0; i < milliseconds; i += 10) {
            //RenderManager.addRenderBox(pos, new BoxColor(0, 1, 0, 1, 0, 1, 0, 0.5f));

            try {
                thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }
    }

    public static boolean needSneak(Block in) {
        return shiftBlocks.contains(in);
    }

    public static boolean pickUpPlaceBlocks(boolean swap, List<Block> needBlocks) {
        if (mc.player.getMainHandStack().getItem() instanceof BlockItem item) {
            if (isNeedBlock(item.getBlock(), needBlocks)) return true;
        }

        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem blockItem) {
                if (isNeedBlock(blockItem.getBlock(), needBlocks)) {
                    if (swap) {
                        if (i < 9) {
                            InventoryUtils.swapItem(i);
                        } else {
                            for (int a = 0; a < 9; a++) {
                                if (mc.player.getInventory().getStack(a).getItem() == Items.AIR) {
                                    InventoryUtils.swapItemOnInventory(a, i);
                                    mc.interactionManager.tick();
                                    InventoryUtils.swapItem(a);
                                    return true;
                                }
                            }
                            InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, i);
                            mc.interactionManager.tick();
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isNeedBlock(Block block, List<Block> needBlocks) {
        BlockState state = block.getDefaultState();
        if (shiftBlocks.contains(block)) return false;
        if (block instanceof AbstractChestBlock<?> || block instanceof ShulkerBoxBlock) return false;
        if (needBlocks.isEmpty()) {
            return state.isSolid();
        } else {
            return state.isSolid() && needBlocks.contains(block);
        }
    }

    public static boolean pickUpPlaceBlocks(boolean swap) {
        return pickUpPlaceBlocks(swap, new ArrayList<>());
    }

    public static boolean isPossibleRich(BlockPos blockPos) {
        return MathUtils.getDistance(mc.player.getPos() ,blockPos.toCenterPos()) <= 6;
    }

    public static FacingBlock checkNearBlocksExtended(BlockPos blockPos) {
        FacingBlock ret;

        ret = checkNearBlocks(blockPos);
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, 0, 1));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, 0, -1));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(-1, 0, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(1, 0, 0));
        if (ret != null) return ret;



        ret = checkNearBlocks(blockPos.add(-2, 0, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(2, 0, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, 0, 2));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, 0, -2));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, -1, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(1, -1, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(-1, -1, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, -1, 1));
        if (ret != null) return ret;

        return checkNearBlocks(blockPos.add(0, -1, -1));
    }

    public static FacingBlock checkNearBlocks(@NotNull BlockPos blockPos) {
        BlockPos playerPos = new ModifyBlockPos(mc.player.getX(), mc.player.getY() - 1, mc.player.getZ());

        FacingBlock facingBlock = null;
        FacingBlock tempFacingBlock;

        tempFacingBlock = checkNearBlock(blockPos.add(0, -1, 0), Direction.UP, playerPos);
        if (tempFacingBlock != null)
            facingBlock = tempFacingBlock;

        tempFacingBlock = checkNearBlock(blockPos.add(-1, 0, 0), Direction.EAST, playerPos);
        if (tempFacingBlock != null)
            facingBlock = tempFacingBlock;

        tempFacingBlock = checkNearBlock(blockPos.add(1, 0, 0), Direction.WEST, playerPos);
        if (tempFacingBlock != null)
            facingBlock = tempFacingBlock;

        tempFacingBlock = checkNearBlock(blockPos.add(0, 0, 1), Direction.NORTH, playerPos);
        if (tempFacingBlock != null)
            facingBlock = tempFacingBlock;

        tempFacingBlock = checkNearBlock(blockPos.add(0, 0, -1), Direction.SOUTH, playerPos);
        if (tempFacingBlock != null)
            facingBlock = tempFacingBlock;

        return facingBlock;
    }

    public static FacingBlock checkNearBlock(BlockPos pos, Direction direction, BlockPos playerPos) {
        if ((!mc.world.getBlockState(pos).isSolid()) || pos == playerPos || pos.getY() > 319) return null;
        float[] rot = AimBotUtils.rotations(pos);
        Direction _direction = AimBotUtils.getFacing(rot[0], rot[1], false);
        if (_direction == direction) {
            return null;
        } else {
            return new FacingBlock(pos, direction);
        }
    }
}