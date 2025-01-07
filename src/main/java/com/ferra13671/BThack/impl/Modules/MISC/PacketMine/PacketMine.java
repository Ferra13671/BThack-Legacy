package com.ferra13671.BThack.impl.Modules.MISC.PacketMine;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.api.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.BThack.api.Animation.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ferra13671.BThack.BThack.initLog;

public class PacketMine extends Module {

    public static ModeSetting page;

    public static BooleanSetting swingHand;
    public static BooleanSetting visibleBreaking;
    public static BooleanSetting packetRotate;
    public static BooleanSetting clientDestroy;
    public static BooleanSetting extraPackets;

    public static BooleanSetting removeUse;
    public static BooleanSetting stopPackets;

    public static BooleanSetting speedMine;
    public static NumberSetting mineSpeed;

    public static BooleanSetting autoCityMode;
    public static BooleanSetting friends;

    public static BooleanSetting conveyorMode;
    public static BooleanSetting conveyorLimitState;
    public static NumberSetting conveyorLimit;

    public static BooleanSetting doubleMode;
    public static NumberSetting fastSpeed;
    public static NumberSetting normalSpeed;
    public static BooleanSetting switchToOld;

    public static BooleanSetting rebreak;
    public static BooleanSetting instaRebreak;

    public static BooleanSetting inventoryMode;
    public static NumberSetting hotbarSlot;

    public static BooleanSetting renderBox;
    public static NumberSetting boxRed;
    public static NumberSetting boxGreen;
    public static NumberSetting boxBlue;
    public static BooleanSetting conveyorRender;
    public static NumberSetting conveyorAlpha;


    public PacketMine() {
        super("PacketMine",
                "lang.module.PacketMine",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        page = new ModeSetting("Page", this, new ArrayList<>(Arrays.asList("General", "Render")));

        swingHand = new BooleanSetting("Swing Hand", this, false, () -> page.getValue().equals("General"));
        visibleBreaking = new BooleanSetting("Visible Breaking", this, false, () -> page.getValue().equals("General"));
        packetRotate = new BooleanSetting("Packet Rotate", this, true, () -> page.getValue().equals("General"));
        clientDestroy = new BooleanSetting("Client Destroy", this, false, () -> page.getValue().equals("General"));
        extraPackets = new BooleanSetting("Extra Packets", this, false, () -> page.getValue().equals("General"));

        removeUse = new BooleanSetting("Remove If Use", this, true, () -> page.getValue().equals("General"));
        stopPackets = new BooleanSetting("Stop Packets", this, true, () -> removeUse.getValue() && page.getValue().equals("General"));

        speedMine = new BooleanSetting("Speed Mine", this, false, () -> !doubleMode.getValue() && page.getValue().equals("General"));
        mineSpeed = new NumberSetting("Mine Speed", this, 1.2, 1, 10, false, () -> speedMine.getValue() && !doubleMode.getValue() && page.getValue().equals("General"));

        doubleMode = new BooleanSetting("Double Mode", this, false, () -> conveyorMode.getValue() && page.getValue().equals("General"));
        fastSpeed = new NumberSetting("Fast Speed", this, 5, 5, 50, false, () -> conveyorMode.getValue() && doubleMode.getValue() && page.getValue().equals("General"));
        normalSpeed = new NumberSetting("Normal Speed", this, 1.05, 0.9, 1.3, false, () -> conveyorMode.getValue() && doubleMode.getValue() && page.getValue().equals("General"));
        switchToOld = new BooleanSetting("Switch To Old", this, false, () -> conveyorMode.getValue() && doubleMode.getValue() && page.getValue().equals("General"));

        rebreak = new BooleanSetting("Rebreak", this, false, () -> page.getValue().equals("General"));
        instaRebreak = new BooleanSetting("Instant", this, false, () -> page.getValue().equals("General"));

        conveyorMode = new BooleanSetting("Conveyor Mode", this, false, () -> page.getValue().equals("General"));
        conveyorLimitState = new BooleanSetting("ConveyorLimit", this, false, () -> page.getValue().equals("General"));
        conveyorLimit = new NumberSetting("Limit", this, 2, 1, 10, true, () -> page.getValue().equals("General") && conveyorLimitState.getValue());

        autoCityMode = new BooleanSetting("Auto City", this, false, () -> page.getValue().equals("General"));
        friends = new BooleanSetting("Friends", this, false, () -> autoCityMode.getValue() && page.getValue().equals("General"));

        inventoryMode = new BooleanSetting("Inventory Mode", this, false, () -> page.getValue().equals("General"));
        hotbarSlot = new NumberSetting("Hotbar Slot", this, 1, 1, 9, true, () ->inventoryMode.getValue() && page.getValue().equals("General"));

        renderBox = new BooleanSetting("Render Box", this, true, () -> page.getValue().equals("Render"));
        boxRed = new NumberSetting("Box Red", this, 0, 0, 255, true, () -> renderBox.getValue() && page.getValue().equals("Render"));
        boxGreen = new NumberSetting("Box Green", this, 255, 0, 255, true, () -> renderBox.getValue() && page.getValue().equals("Render"));
        boxBlue = new NumberSetting("Box Blue", this, 0, 0, 255, true, () -> renderBox.getValue() && page.getValue().equals("Render"));
        conveyorRender = new BooleanSetting("Conveyor Render", this, true, () -> renderBox.getValue() && page.getValue().equals("Render"));
        conveyorAlpha = new NumberSetting("Conv. Alpha", this, 255, 0, 255, true, () -> renderBox.getValue() && conveyorRender.getValue() && page.getValue().equals("Render"));


        initSettings(
                page,

                swingHand,
                visibleBreaking,
                packetRotate,
                clientDestroy,
                extraPackets,

                removeUse,
                stopPackets,

                speedMine,
                mineSpeed,

                doubleMode,
                fastSpeed,
                normalSpeed,
                switchToOld,

                rebreak,
                instaRebreak,

                autoCityMode,
                friends,

                conveyorMode,
                conveyorLimitState,
                conveyorLimit,

                inventoryMode,
                hotbarSlot,

                renderBox,
                boxRed,
                boxGreen,
                boxBlue
        );

    }
    private final Animation conveyorAnimation = new Animation(Easing.LINEAR, 1000);
    private boolean animationInvert = true;

    private boolean doubleFast;
    private BreakingBlock doubleBlock;
    private boolean firstSkip = true;

    public BreakingBlock currentBreakingBlock;
    public final ArrayList<BreakingBlock> conveyorBlocks = new ArrayList<>();
    private float destroyDelta = 0;

    private int currentInventoryModeSlot = -1;
    private int currentInventoryModeHotbarSlot = -1;

    BlockPos breakedPos;

    @Override
    public void onEnable() {
        super.onEnable();

        currentBreakingBlock = null;
        conveyorBlocks.clear();
        currentInventoryModeSlot = -1;
        doubleFast = true;
        firstSkip = true;

        ModuleList.superInstaMine.setToggled(false);

        breakedPos = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        currentBreakingBlock = null;
        conveyorBlocks.clear();
        if (!nullCheck())
            packetRemoveItem();
        doubleFast = true;
        firstSkip = true;
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck() || DestroyManager.isDestroying) return;
        if (e.getBlockPos() == null) return;

        e.setCancelled(true);
        if (currentBreakingBlock != null) {
            if (currentBreakingBlock.blockPos.equals(e.getBlockPos())) return;
        }
        updateBlockLimited(e.getBlockPos());
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onUseBlock(UseBlockEvent e) {
        if (e.blockHitResult.getBlockPos() == null) return;
        if (currentBreakingBlock == null) return;
        if (!removeUse.getValue()) return;
        if (conveyorMode.getValue() && conveyorContains(e.getBlockHitResult().getBlockPos())) {
            conveyorRemove(e.getBlockHitResult().getBlockPos());
            e.setCancelled(true);
        }
        if (e.blockHitResult.getBlockPos() == currentBreakingBlock.blockPos) {
            e.setCancelled(true);
            if (stopPackets.getValue()) {
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, currentBreakingBlock.blockPos, Direction.DOWN));
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, currentBreakingBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
            }

            currentBreakingBlock = null;
        }
    }
    public void updateBlockLimited(BlockPos pos) {  //Okay
        if (!conveyorLimitState.getValue()) {
            updateBlock(pos);
            return;
        }
        if (conveyorBlocks.size() >= conveyorLimit.getValue() + 1) {
            if (conveyorMode.getValue()) {
                conveyorBlocks.remove(0);
            }
            if (pos == currentBreakingBlock.blockPos) {
                if (stopPackets.getValue()) {
                    mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, currentBreakingBlock.blockPos, Direction.DOWN));
                    mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, currentBreakingBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                }
                currentBreakingBlock = null;
            }
        }
        else updateBlock(pos);
    }
    public boolean updateBlock(BlockPos pos) {
        if (conveyorMode.getValue()) {
            if (currentBreakingBlock != null) {
                if (!conveyorContains(pos)) {
                    conveyorBlocks.add(new BreakingBlock(pos));
                    return true;
                }
                return false;
            }
        }


        currentBreakingBlock = new BreakingBlock(pos);
        return true;
    }

    private void checkDestroyDelta() {
        int oldSlot = mc.player.getInventory().selectedSlot;

        int bestSlot = AutoTool.getBestSlot(mc.world.getBlockState(currentBreakingBlock.blockPos), inventoryMode.getValue() ? 36 : 9);
        if (bestSlot != -1) {
            if (bestSlot < 9) {
                InventoryUtils.swapItem(bestSlot);
            } else {
                InventoryUtils.swapItemOnInventory(oldSlot, bestSlot);
            }
        }


        boolean ground = mc.player.onGround;
        mc.player.onGround = true;

        destroyDelta = mc.world.getBlockState(currentBreakingBlock.blockPos).calcBlockBreakingDelta(mc.player, mc.player.getWorld(), currentBreakingBlock.blockPos);
        mc.player.onGround = ground;

        if (bestSlot < 9) {
            InventoryUtils.swapItem(oldSlot);
        } else {
            InventoryUtils.swapItemOnInventory(oldSlot, bestSlot);
        }
    }

    @EventSubscriber
    public void onRender(RenderWorldEvent.Last e) {
        if (!renderBox.getValue() || currentBreakingBlock == null) return;

        if (conveyorAnimation.getEase() >= 1) {
            conveyorAnimation.reset();
            animationInvert = !animationInvert;
        }

        ArrayList<RenderBox> renderBoxes = new ArrayList<>();
        double currentDestroyBlockSize = currentBreakingBlock.currentDestroyProgress / 2;
        float boxR = (float) boxRed.getValue() / 255f;
        float boxG = (float) boxGreen.getValue() / 255f;
        float boxB = (float) boxBlue.getValue() / 255f;
        renderBoxes.add(
                new RenderBox(
                        BlockUtils.createBox(
                                currentBreakingBlock.blockPos,
                                currentDestroyBlockSize,
                                currentDestroyBlockSize,
                                currentDestroyBlockSize,
                                true
                        ),
                        boxR,
                        boxG,
                        boxB,
                        1,
                        boxR,
                        boxG,
                        boxB,
                        0.3f
                )
        );

        float conveyorLinesAlpha = (float) ((conveyorAlpha.getValue() / 255d) * (animationInvert ? 1 - conveyorAnimation.getEase() : conveyorAnimation.getEase()));
        float conveyorBoxAlpha = 0.3f * conveyorLinesAlpha;

        if (conveyorMode.getValue()) {
            if (doubleMode.getValue() && doubleBlock != null) {
                if (BlockUtils.canBreak(doubleBlock.blockPos)) {
                    double doubleBlockSize = doubleBlock.currentDestroyProgress / 2;
                    renderBoxes.add(
                            new RenderBox(
                                    BlockUtils.createBox(
                                            doubleBlock.blockPos,
                                            doubleBlockSize,
                                            doubleBlockSize,
                                            doubleBlockSize,
                                            true
                                    ),
                                    boxR,
                                    boxG,
                                    boxB,
                                    1,
                                    boxR,
                                    boxG,
                                    boxB,
                                    0.3f
                            )
                    );
                }
            }
            if (conveyorRender.getValue()) {
                for (BreakingBlock pos : conveyorBlocks) {
                    renderBoxes.add(
                            new RenderBox(
                                    BlockUtils.createBox(
                                            pos.blockPos,
                                            0.1,
                                            0.1,
                                            0.1,
                                            true
                                    ),
                                    1,
                                    1,
                                    0,
                                    conveyorLinesAlpha,
                                    1,
                                    1,
                                    0,
                                    conveyorBoxAlpha
                            )
                    );
                }
            }
        }

        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(renderBoxes);
        BThackRender.boxRender.stopBoxRender();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || mc.isPaused()) return;

        if (rebreak.getValue() && breakedPos != null){
            if (!mc.world.isAir(breakedPos) && currentBreakingBlock == null) {
                updateBlockLimited(breakedPos);
            }
        }

        if ((!conveyorMode.getValue() || conveyorBlocks.isEmpty()) && currentBreakingBlock == null) doubleBlock = null;

        if (!conveyorMode.getValue())
            if (!conveyorBlocks.isEmpty())
                conveyorBlocks.clear();

        if (conveyorBlocks.isEmpty()) firstSkip = true;

        if (autoCityMode.getValue()) {
            autoCityAction();
        }


        if (currentBreakingBlock == null) {
            doubleFast = true;
            destroyDelta = 0;
            return;
        }

        if (destroyDelta == 0) {
            checkDestroyDelta();
        }


        if (!updateBreak(currentBreakingBlock, true)) {

            packetRemoveItem();

            if (conveyorMode.getValue()) {
                for (BreakingBlock breakingBlock : conveyorBlocks) {
                    if (breakingBlock.equals(currentBreakingBlock)) {
                        conveyorBlocks.remove(breakingBlock);
                        break;
                    }
                }
                if (!firstSkip) {
                    conveyorBlocks.remove(0);
                } else {
                    firstSkip = false;
                }
                breakedPos = currentBreakingBlock.blockPos;
                currentBreakingBlock = null;
                if (!conveyorBlocks.isEmpty()) {
                    updateBlock(conveyorBlocks.get(0).blockPos);
                }
            } else {
                breakedPos = currentBreakingBlock.blockPos;
                currentBreakingBlock = null;
            }
        }
    }

    public boolean updateBreak(BreakingBlock breakingBlock, boolean reset) {
        Block block = mc.world.getBlockState(breakingBlock.blockPos).getBlock();

        if (mc.world.isAir(breakingBlock.blockPos) || block == Blocks.BEDROCK) {
            packetRemoveItem();

            if (reset) destroyDelta = 0;
            return false;
        }

        if (packetRotate.getValue()) {
            float[] rot = AimBotUtils.rotations(breakingBlock.blockPos);

            GrimUtils.sendPreActionGrimPackets(rot[0], rot[1]);
        }

        if (reset && currentInventoryModeSlot == -1) {
            packetEquipItem();
        }

        try {
            if (!breakingBlock.startDestroying) {
                if (extraPackets.getValue()) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.yaw, mc.player.pitch, true));
                    initLog("FULL");
                }

                if (instaRebreak.getValue() && breakedPos != null && breakedPos.equals(breakingBlock.blockPos)){
                    breakingBlock.currentDestroyProgress = 1;
                    mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakingBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                    initLog("STOP_ACTION");
                    breakingBlock.startDestroying = true;
                } else{
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakingBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                initLog("START_ACTION");
                breakingBlock.startDestroying = true;
                if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
                }

            } else {
                if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
                breakingBlock.currentDestroyProgress += destroyDelta * (doubleMode.getValue() && conveyorMode.getValue() && !conveyorBlocks.isEmpty() ? (doubleFast && (firstSkip || conveyorBlocks.size() > 1) ? fastSpeed.getValue() : normalSpeed.getValue()) : (speedMine.getValue() ? mineSpeed.getValue() : 1));
                if (visibleBreaking.getValue()) mc.world.setBlockBreakingInfo(mc.player.getId(), breakingBlock.blockPos, (int)(breakingBlock.currentDestroyProgress * 10.0F));
            }
            if (breakingBlock.currentDestroyProgress >= 1) {
                if (instaRebreak.getValue() && breakedPos != null && breakedPos.equals(breakingBlock.blockPos)){
                    packetRemoveItem();
                    breakingBlock.currentDestroyProgress = 1;
                    return false;
                }
                breakingBlock.currentDestroyProgress = 1;
                if (extraPackets.getValue()) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.yaw, mc.player.pitch, true));
                    initLog("FULL");
                }

                if (clientDestroy.getValue())
                    mc.interactionManager.breakBlock(breakingBlock.blockPos);
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, breakingBlock.blockPos, Direction.DOWN));
                initLog("ABORT_ACTION");
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakingBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                initLog("STOP_ACTION");

                packetRemoveItem();


                if (reset) destroyDelta = 0;
                doubleFast = !doubleFast;
                if (!doubleFast) {
                    doubleBlock = currentBreakingBlock;
                } else {
                    if (doubleBlock != null && conveyorMode.getValue() && doubleMode.getValue() && switchToOld.getValue()) {
                        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, doubleBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                        //mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, doublePos, Direction.DOWN));
                        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, doubleBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                    }
                }
                return false;
            }
        } catch (Exception ignored) {}

        if (breakingBlock.blockPos != null) {
            if (mc.world.isAir(breakingBlock.blockPos)) {
                if (reset) destroyDelta = 0;
                return false;
            }
        }
        return true;
    }

    private final List<Vec3i> autoCityVectors = Arrays.asList(
            new Vec3i(1,0,0),
            new Vec3i(0,0,1),
            new Vec3i(-1,0,0),
            new Vec3i(0,0,-1)
    );
    private void autoCityAction() {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;
            if (!friends.getValue()) {
                if (SocialManagers.FRIENDS.contains(player)) continue;
            }
            if (player.distanceTo(mc.player) > 4) continue;
            BlockPos blockPos = new ModifyBlockPos(player.getX(), player.getY(),player.getZ());
            if (BlockUtils.canBreak(blockPos) && MathUtils.getDistance(mc.player.getPos(), blockPos.toCenterPos()) < 4.25)
                updateBlockLimited(blockPos);
            BlockPos nearestPos = null;
            double nearestLength = 9999;
            for (Vec3i vec : autoCityVectors) {
                BlockPos checkPos = new ModifyBlockPos(player.getX() + vec.getX(), player.getY(), player.getZ() + vec.getZ());
                double length = MathUtils.getDistance(mc.player.getPos(), checkPos.toCenterPos());
                if (length > 4.25) continue;
                if (length < nearestLength) {
                    nearestLength = length;
                    nearestPos = checkPos;
                }
            }
            if (nearestPos == null) return;
            if (BlockUtils.canBreak(nearestPos)) updateBlockLimited(nearestPos);
        }
    }

    private void packetEquipItem() {
        int bestSlot = AutoTool.getBestSlot(mc.world.getBlockState(currentBreakingBlock.blockPos), inventoryMode.getValue() ? 36 : 8);
        if (bestSlot != -1) {
            if (bestSlot < 9) {
                mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(bestSlot));
            } else {
                currentInventoryModeHotbarSlot = (int) hotbarSlot.getValue() - 1;
                pc.packetClickSlot(0, bestSlot, currentInventoryModeHotbarSlot, SlotActionType.SWAP);
                currentInventoryModeSlot = bestSlot;
                mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentInventoryModeHotbarSlot));
            }
        }
    }

    private void packetRemoveItem() {
        if (currentInventoryModeSlot != -1 && currentInventoryModeHotbarSlot != -1) {
            pc.packetClickSlot(0, currentInventoryModeSlot, currentInventoryModeHotbarSlot, SlotActionType.SWAP);
            currentInventoryModeSlot = -1;
            currentInventoryModeHotbarSlot = -1;
        }
        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
    }

    private boolean conveyorContains(BlockPos pos) {
        for (BreakingBlock breakingBlock : conveyorBlocks) {
            if (breakingBlock.blockPos.equals(pos))
                return true;
        }
        return false;
    }

    private void conveyorRemove(BlockPos pos) {
        for (BreakingBlock breakingBlock : conveyorBlocks) {
            if (breakingBlock.blockPos == pos) {
                conveyorBlocks.remove(breakingBlock);
                return;
            }
        }
    }
}