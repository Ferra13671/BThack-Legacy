package com.ferra13671.BThack.managers.impl.place;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.*;
import com.ferra13671.BThack.api.utils.rotate.RotateMode;
import com.ferra13671.BThack.api.utils.rotate.RotateUtils;
import com.ferra13671.BThack.mixins.accessor.IAbstractBlock;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.google.common.collect.Sets;
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

import java.util.*;

public class PlaceManager implements Initializable, Mc {
    public static final Set<Block> ignoreBlocks = Sets.newHashSet(
            Blocks.AIR,
            Blocks.CAVE_AIR,
            Blocks.VOID_AIR,
            Blocks.WATER,
            Blocks.LAVA
    );
    public static final List<Block> shiftBlocks = Arrays.asList(
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
    public static final List<Block> obsidians = Arrays.asList(
            Blocks.OBSIDIAN,
            Blocks.CRYING_OBSIDIAN
    );

    public static boolean isBuilding = false;

    protected final List<BlockPos> blockPoses = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
        BThack.debug("Build Manager inited!");
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (blockPoses.isEmpty()) return;
        synchronized (blockPoses) {
            if (Module.nullCheck()) {
                blockPoses.clear();
                return;
            }

            blockPoses.forEach(pos -> placeBlock(pos, RotateMode.GRIM));
            blockPoses.clear();
        }
    }

    @SuppressWarnings({"DataFlowIssue", "deprecation"})
    public static void placeBlock(BlockPos pos, RotateMode rotateMode) {
        try {
            if (!mc.world.getBlockState(pos).isReplaceable()) return;
            BlockState block1 = mc.world.getBlockState(pos);
            if ((!mc.world.isAir(pos) && !block1.isLiquid()) || block1.isSolid()) return;

            BlockHitResult bhr = getHitResult(pos, true, Direction.UP);
            if (bhr == null) return;

            float[] rotations = RotateUtils.rotations(bhr.getPos());
            boolean sneak = PlaceManager.needSneak(mc.world.getBlockState(bhr.getBlockPos()).getBlock()) && !mc.player.isSneaking();

            if (sneak)
                Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));

            rotateMode.preRotate(rotations[0], rotations[1]);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
            Managers.NETWORK_MANAGER.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

            if (sneak)
                Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
            rotateMode.postRotate();
        } catch (Exception ignored) {}
    }

    public static BlockHitResult getHitResult(BlockPos pos) {
        return getHitResult(pos, true, Direction.UP);
    }

    public static BlockHitResult getHitResult(BlockPos blockPos, boolean check, Direction direction) {
        com.ferra13671.BThack.managers.impl.place.FacingBlock block = check ? checkNearBlocksExtended(blockPos) : new com.ferra13671.BThack.managers.impl.place.FacingBlock(blockPos, direction);
        if (block == null) return null;
        return getHitResult(block);
    }

    public static BlockHitResult getHitResult(com.ferra13671.BThack.managers.impl.place.FacingBlock facingBlock) {
        Vec3d pos = facingBlock.pos().toCenterPos();
        return new BlockHitResult(new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(new Vec3d(facingBlock.direction().getVector()).multiply(0.5)), facingBlock.direction(), facingBlock.pos(), false);
    }

    @SuppressWarnings("DataFlowIssue")
    protected static boolean check(ArrayList<BlockPos> positions) {
        for (BlockPos pos : positions) {
            com.ferra13671.BThack.managers.impl.place.FacingBlock fBlock = PlaceManager.checkNearBlocksExtended(pos);
            if (fBlock == null) {
                continue;
            }
            if (ignoreBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
                return true;
            }
        }
        return false;
    }

    public static boolean needSneak(Block in) {
        return shiftBlocks.contains(in);
    }

    @SuppressWarnings("DataFlowIssue")
    public static boolean pickUpPlaceBlocks(boolean swap, List<Block> needBlocks) {
        if (mc.player.getMainHandStack().getItem() instanceof BlockItem item)
            if (isNeedBlock(item.getBlock(), needBlocks)) return true;

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

    @SuppressWarnings("deprecation")
    public static boolean isNeedBlock(Block block, List<Block> needBlocks) {
        BlockState state = block.getDefaultState();
        if (shiftBlocks.contains(block) || !((IAbstractBlock) block).getCollidable()) return false;
        if (block instanceof AbstractChestBlock<?> || block instanceof ShulkerBoxBlock) return false;
        if (needBlocks.isEmpty())
            return state.isSolid();
        else
            return state.isSolid() && needBlocks.contains(block);

    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean pickUpPlaceBlocks(boolean swap) {
        return pickUpPlaceBlocks(swap, new ArrayList<>());
    }

    @SuppressWarnings("DataFlowIssue")
    public static boolean isPossibleRich(BlockPos blockPos) {
        return MathUtils.getDistance(mc.player.getPos() ,blockPos.toCenterPos()) <= 6;
    }

    public static com.ferra13671.BThack.managers.impl.place.FacingBlock checkNearBlocksExtended(BlockPos blockPos) {
        com.ferra13671.BThack.managers.impl.place.FacingBlock ret;

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

    @SuppressWarnings("DataFlowIssue")
    public static com.ferra13671.BThack.managers.impl.place.FacingBlock checkNearBlocks(BlockPos blockPos) {
        BlockPos playerPos = BlockPos.ofFloored(mc.player.getX(), mc.player.getY() - 1, mc.player.getZ());

        com.ferra13671.BThack.managers.impl.place.FacingBlock facingBlock = null;
        com.ferra13671.BThack.managers.impl.place.FacingBlock tempFacingBlock;

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

    @SuppressWarnings({"DataFlowIssue", "deprecation"})
    public static com.ferra13671.BThack.managers.impl.place.FacingBlock checkNearBlock(BlockPos pos, Direction direction, BlockPos playerPos) {
        if ((!mc.world.getBlockState(pos).isSolid()) || pos == playerPos || pos.getY() > 319) return null;
        if (!checkCanPlaceAtDirection(pos, direction))
            return null;
        else
            return new FacingBlock(pos, direction);
    }

    @SuppressWarnings("DataFlowIssue")
    private static boolean checkCanPlaceAtDirection(BlockPos pos, Direction direction) {
        Vec3d vec3d = pos.toCenterPos();
        vec3d.x += direction.getOffsetX() / 2d;
        vec3d.y += direction.getOffsetY() / 2d;
        vec3d.z += direction.getOffsetZ() / 2d;
        return checkCanPlaceAtDirectionInternal(mc.player.getX(), vec3d.x, direction.getOffsetX()) &&
                checkCanPlaceAtDirectionInternal(mc.player.getY() + mc.player.getStandingEyeHeight(), vec3d.y, direction.getOffsetY()) &&
                checkCanPlaceAtDirectionInternal(mc.player.getZ(), vec3d.z, direction.getOffsetZ());
    }

    private static boolean checkCanPlaceAtDirectionInternal(double playerCord, double blockCord, double cordOffset) {
        if (cordOffset == 0) return true;
        if (cordOffset > 0) return playerCord > blockCord;
        else return playerCord < blockCord;
    }
}