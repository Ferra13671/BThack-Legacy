package com.ferra13671.BThack.impl.Modules.MISC.PacketMine;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.api.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketMine extends Module {

    public final CategorySetting generalCategory = new CategorySetting("General", this);
    public final CategorySetting renderCategory = new CategorySetting("Render", this);

    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, true).inCategory(generalCategory);

    public final BooleanSetting removeIfUse = new BooleanSetting("Remove If Use", this, true).inCategory(generalCategory);
    public final NumberSetting breakDelaySet = new NumberSetting("Break Delay", this, 0, 0, 5, true).inCategory(generalCategory);

    public final BooleanSetting conveyorMode = new BooleanSetting("Conveyor Mode", this, false).inCategory(generalCategory);

    public final BooleanSetting doubleMine = new BooleanSetting("Double Mine", this, false).inCategory(generalCategory);
    public final NumberSetting doubleSpeed = new NumberSetting("Double Speed", this, 0.85, 0.5, 1, false, doubleMine::getValue).inCategory(generalCategory);

    public final BooleanSetting instaRebreak = new BooleanSetting("Insta Rebreak", this, false).inCategory(generalCategory);

    public final BooleanSetting speedMine = new BooleanSetting("Speed Mine", this, false, () -> !doubleMine.getValue()).inCategory(generalCategory);
    public final NumberSetting mineSpeed = new NumberSetting("Mine Speed", this, 1.2, 1, 10, false, () -> speedMine.getValue() && !doubleMine.getValue()).inCategory(generalCategory);

    public final BooleanSetting autoCityMode = new BooleanSetting("Auto City", this, false).inCategory(generalCategory);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false, autoCityMode::getValue).inCategory(generalCategory);

    public final BooleanSetting inventoryMode = new BooleanSetting("Inventory Mode", this, false).inCategory(generalCategory);
    public final NumberSetting hotbarSlot = new NumberSetting("Hotbar Slot", this, 1, 1, 9, true, inventoryMode::getValue).inCategory(generalCategory);


    public final BooleanSetting renderBox = new BooleanSetting("Render Box", this, true).inCategory(renderCategory);
    public final ColorSetting boxColor = new ColorSetting("Box Color", this, new Color(0, 255, 0), renderBox::getValue).withBlockedAlpha().inCategory(renderCategory);
    public final BooleanSetting conveyorRender = new BooleanSetting("Conveyor Render", this, true, renderBox::getValue).inCategory(renderCategory);

    public final NumberSetting conveyorAlpha = new NumberSetting("Conv. Alpha", this, 255, 0, 255, true, () -> renderBox.getValue() && conveyorRender.getValue()).inCategory(renderCategory);


    public PacketMine() {
        super("PacketMine",
                "lang.module.PacketMine",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                generalCategory,
                renderCategory
        );

    }

    //Basic variables
    public BreakingBlock currentBreakingBlock;
    private float destroyDelta = 0;
    private int breakDelay = 5;
    private boolean itemRemoved = true;

    //Conveyor Mode
    public final ArrayList<BreakingBlock> conveyorBlocks = new ArrayList<>();

    //Conveyor Animation
    private final Animation conveyorAnimation = new Animation(Easing.LINEAR, 1000);
    private boolean conveyorAnimationInvert = true;

    //Double Mode
    private BreakingBlock doubleBreakingBlock;

    //Inventory mode
    private int currentSlot = -1;
    private int currentHotbarSlot = -1;

    //Insta Rebreak
    BlockPos breakedPos;

    //Insta Rebreak Animation
    private final Animation instaRebreakAnimation = new Animation(Easing.LINEAR, 1000);
    private boolean instaRebreakAnimationInvert = true;

    @Override
    public void onEnable() {
        super.onEnable();

        clearBreakBlocks();
        doubleBreakingBlock = null;
        conveyorBlocks.clear();
        currentSlot = -1;
        breakDelay = breakDelaySet.getValue().intValue();

        ModuleList.superInstaMine.setToggled(false);

        conveyorAnimation.reset();
        conveyorAnimationInvert = true;

        instaRebreakAnimation.reset();
        instaRebreakAnimationInvert = true;

        breakedPos = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        clearBreakBlocks();
        doubleBreakingBlock = null;
        conveyorBlocks.clear();
        if (!nullCheck())
            packetRemoveItem();

        breakDelay = breakDelaySet.getValue().intValue();

        ModuleList.treeCutter.setToggled(false);
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck() || DestroyManager.isDestroying) return;
        if (e.getBlockPos() == null) return;

        e.setCancelled(true);
        if (currentBreakingBlock != null) {
            if (currentBreakingBlock.blockPos.equals(e.getBlockPos())) return;
        }
        updateBlock(e.getBlockPos());
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onUseBlock(UseBlockEvent e) {
        if (e.blockHitResult.getBlockPos() == null) return;
        if (currentBreakingBlock == null) return;
        if (!removeIfUse.getValue()) return;
        if (conveyorMode.getValue() && conveyorContains(e.getBlockHitResult().getBlockPos())) {
            conveyorRemove(e.getBlockHitResult().getBlockPos());
            e.setCancelled(true);
        }
    }

    public void updateBlock(BlockPos pos) {
        if (conveyorMode.getValue()) {
            if (needAddToConveyor()) {
                if (!conveyorContains(pos))
                    conveyorBlocks.add(new BreakingBlock(pos));
                return;
            }
        }


        updateBlockInternal(new BreakingBlock(pos));
    }

    private void updateBlockInternal(BreakingBlock breakingBlock) {
        if (doubleMine.getValue() && currentBreakingBlock != null) {
            doubleBreakingBlock = currentBreakingBlock;
        }
        currentBreakingBlock = breakingBlock;
    }

    private void checkDestroyDelta() {
        int bestSlot = AutoTool.getBestSlot(mc.world.getBlockState(currentBreakingBlock.blockPos), inventoryMode.getValue() ? 36 : 9);
        ItemStack stack = mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot);
        if (bestSlot != -1) {
            stack = mc.player.getInventory().getStack(bestSlot);
        }

        destroyDelta = ItemUtils.getMineSpeed(mc.world.getBlockState(currentBreakingBlock.blockPos), currentBreakingBlock.blockPos, stack);
    }

    @EventSubscriber
    public void onRender(RenderWorldLastEvent e) {
        ArrayList<RenderBox> renderBoxes = new ArrayList<>();

        if (instaRebreakAnimation.getEase() >= 1) {
            instaRebreakAnimation.reset();
            instaRebreakAnimationInvert = !instaRebreakAnimationInvert;
        }

        if (instaRebreak.getValue() && breakedPos != null) {
            float instaRebreakAlpha = (float) (instaRebreakAnimationInvert ? 1 - instaRebreakAnimation.getEase() : instaRebreakAnimation.getEase());
            renderBoxes.add(new RenderBox(
                    BlockUtils.createBox(breakedPos, 0.5, 0.5, 1, false),
                    1,
                    0,
                    0,
                    instaRebreakAlpha,
                    1,
                    0,
                    0,
                    instaRebreakAlpha * 0.3f));
        }

        if (renderBox.getValue() && currentBreakingBlock != null) {
            if (conveyorAnimation.getEase() >= 1) {
                conveyorAnimation.reset();
                conveyorAnimationInvert = !conveyorAnimationInvert;
            }

            double currentDestroyBlockSize = createLerpSize(currentBreakingBlock);
            float boxR = (float) boxColor.getValue().getRed() / 255f;
            float boxG = (float) boxColor.getValue().getGreen() / 255f;
            float boxB = (float) boxColor.getValue().getBlue() / 255f;
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

            float conveyorLinesAlpha = (float) ((conveyorAlpha.getValue() / 255d) * (conveyorAnimationInvert ? 1 - conveyorAnimation.getEase() : conveyorAnimation.getEase()));
            float conveyorBoxAlpha = 0.3f * conveyorLinesAlpha;

            if (doubleMine.getValue() && doubleBreakingBlock != null) {
                if (BlockUtils.canBreak(doubleBreakingBlock.blockPos)) {
                    double doubleBlockSize = createLerpSize(doubleBreakingBlock);
                    renderBoxes.add(
                            new RenderBox(
                                    BlockUtils.createBox(
                                            doubleBreakingBlock.blockPos,
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
            if (conveyorMode.getValue() && conveyorRender.getValue()) {
                for (BreakingBlock pos : conveyorBlocks) {
                    renderBoxes.add(
                            createRenderBox(BlockUtils.createBox(
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
                                    conveyorBoxAlpha
                            )
                    );
                }
            }
        }

        if (!renderBoxes.isEmpty()) {
            BThackRender.boxRender.prepareBoxRender();
            BThackRender.boxRender.renderBoxes(renderBoxes);
            BThackRender.boxRender.stopBoxRender();
        }
    }

    private RenderBox createRenderBox(Box box, float red, float green, float blue, float linesAlpha, float boxAlpha) {
        return new RenderBox(box, red, green, blue, linesAlpha, red, green, blue, boxAlpha);
    }

    private double createLerpSize(BreakingBlock breakingBlock) {
        return MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(true), breakingBlock.prevDestroyProgress, breakingBlock.currentDestroyProgress) / 2;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || mc.isPaused()) return;

        if (!itemRemoved) {
            packetRemoveItem();
            itemRemoved = true;
        }

        if (instaRebreak.getValue() && breakedPos != null) {
            if (!mc.world.isAir(breakedPos) && BlockUtils.canBreak(breakedPos) && currentBreakingBlock == null) {
                updateBlock(breakedPos);
                breakDelay = 0; //For InstaRebreak, the break delay doesn't make sense
            }
        }

        if ((!conveyorMode.getValue() || conveyorBlocks.isEmpty()) && currentBreakingBlock == null) doubleBreakingBlock = null;

        if (!conveyorMode.getValue())
            if (!conveyorBlocks.isEmpty())
                conveyorBlocks.clear();

        if (autoCityMode.getValue())
            autoCityAction();


        if (currentBreakingBlock == null && (!doubleMine.getValue() || doubleBreakingBlock == null)) {
            destroyDelta = 0;
            return;
        }

        if (destroyDelta == 0)
            checkDestroyDelta();

        if (breakDelay > 0) {
            breakDelay--;
            return;
        }
        if (!updateBreakProgress()) {

            packetRemoveItem();
            itemRemoved = false;

            if (conveyorMode.getValue()) {
                conveyorBlocks.removeIf(breakingBlock -> breakingBlock.equals(currentBreakingBlock) || breakingBlock.equals(doubleBreakingBlock));

                breakedPos = currentBreakingBlock.blockPos;
                clearBreakBlocks();
                if (!conveyorBlocks.isEmpty()) {
                    updateBlock(conveyorBlocks.getFirst().blockPos);
                    conveyorBlocks.removeFirst();
                    if (doubleMine.getValue() && !conveyorBlocks.isEmpty()) {
                        updateBlock(conveyorBlocks.getFirst().blockPos);
                        conveyorBlocks.removeFirst();
                    }
                }
            } else {
                breakedPos = currentBreakingBlock.blockPos;
                clearBreakBlocks();
            }
        }
    }

    public boolean updateBreakProgress() {
        if (currentBreakingBlock == null) return false;
        if (!currentBreakingBlock.canBreak() && (doubleBreakingBlock == null || (!doubleMine.getValue() || !doubleBreakingBlock.canBreak()))) {
            packetRemoveItem();
            destroyDelta = 0;
            return false;
        }
        if (currentSlot == -1) packetEquipItem();

        if (checkStopAction(currentBreakingBlock)) return false;
        if (doubleMine.getValue() && doubleBreakingBlock != null)
            checkStopAction(doubleBreakingBlock);

        updateBreakProgressInternal(currentBreakingBlock);
        if (doubleMine.getValue() && doubleBreakingBlock != null)
            updateBreakProgressInternal(doubleBreakingBlock);

        if (!currentBreakingBlock.canBreak())
            currentBreakingBlock = null;
        if (doubleBreakingBlock != null && !doubleBreakingBlock.canBreak())
            doubleBreakingBlock = null;
        return true;
    }

    public boolean checkStopAction(BreakingBlock breakingBlock) {
        if (breakingBlock.currentDestroyProgress == 1) {
            if (breakingBlock == currentBreakingBlock)
                if (instaRebreak.getValue() && breakedPos != null && breakedPos.equals(currentBreakingBlock.blockPos)) return false;
            destroyDelta = 0;
            if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
            if (!doubleMine.getValue() || breakingBlock == currentBreakingBlock)
                stopDestroyBlock(currentBreakingBlock.blockPos);
            return true;
        }
        return false;
    }

    public void updateBreakProgressInternal(BreakingBlock breakingBlock) {
        if (!breakingBlock.startDestroying) {
            if (breakingBlock == currentBreakingBlock)
                if (doubleMine.getValue() && doubleBreakingBlock != null && !doubleBreakingBlock.startDestroying) return;
            if (instaRebreak.getValue() && breakedPos != null && breakedPos.equals(breakingBlock.blockPos)){
                breakingBlock.currentDestroyProgress = 1;
                if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
                stopDestroyBlock(breakingBlock.blockPos);
            } else {
                if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
                startDestroyBlock(breakingBlock.blockPos);
            }
            breakingBlock.startDestroying = true;
        } else {
            breakingBlock.prevDestroyProgress = breakingBlock.currentDestroyProgress;
            breakingBlock.currentDestroyProgress += getDestroyDelta(breakingBlock == currentBreakingBlock);
            if (breakingBlock.currentDestroyProgress >= 1) breakingBlock.currentDestroyProgress = 1;
        }
    }

    private double getDestroyDelta(boolean isDouble) {
        return (destroyDelta * (isDouble && doubleMine.getValue() ? doubleSpeed.getValue() : 1)) * (speedMine.getValue() ? mineSpeed.getValue() : 1);
    }

    private void startDestroyBlock(BlockPos blockPos) {
        Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, RotateUtils.getInvertedFacingEntity(mc.player), id));
        Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, RotateUtils.getInvertedFacingEntity(mc.player), id));
        if (doubleMine.getValue())
            Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, RotateUtils.getInvertedFacingEntity(mc.player), id));
    }

    private void stopDestroyBlock(BlockPos blockPos) {
        Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, RotateUtils.getInvertedFacingEntity(mc.player), id));
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
                if (Managers.FRIENDS_MANAGER.contains(player)) continue;
            }
            if (player.distanceTo(mc.player) > 4) continue;
            BlockPos blockPos = BlockPos.ofFloored(player.getX(), player.getY(),player.getZ());
            if (BlockUtils.canBreak(blockPos) && MathUtils.getDistance(mc.player.getPos(), blockPos.toCenterPos()) < 4.25)
                updateBlock(blockPos);
            BlockPos nearestPos = null;
            double nearestLength = 9999;
            for (Vec3i vec : autoCityVectors) {
                BlockPos checkPos = BlockPos.ofFloored(player.getX() + vec.getX(), player.getY(), player.getZ() + vec.getZ());
                double length = MathUtils.getDistance(mc.player.getPos(), checkPos.toCenterPos());
                if (length > 4.25) continue;
                if (length < nearestLength) {
                    nearestLength = length;
                    nearestPos = checkPos;
                }
            }
            if (nearestPos == null) return;
            if (BlockUtils.canBreak(nearestPos)) updateBlock(nearestPos);
        }
    }

    private void packetEquipItem() {
        int bestSlot = AutoTool.getBestSlot(mc.world.getBlockState(currentBreakingBlock.blockPos), inventoryMode.getValue() ? 36 : 8);
        if (bestSlot != -1) {
            if (bestSlot < 9) {
                Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(bestSlot));
            } else {
                currentHotbarSlot = hotbarSlot.getValue().intValue() - 1;
                pc.packetClickSlot(0, bestSlot, currentHotbarSlot, SlotActionType.SWAP);
                currentSlot = bestSlot;
                Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(currentHotbarSlot));
            }
        }
    }

    private void packetRemoveItem() {
        if (currentSlot != -1 && currentHotbarSlot != -1) {
            pc.packetClickSlot(0, currentSlot, currentHotbarSlot, SlotActionType.SWAP);
            currentSlot = -1;
            currentHotbarSlot = -1;
        }
        Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
    }

    public boolean conveyorContains(BlockPos pos) {
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

    public void clearBreakBlocks() {
        currentBreakingBlock = null;
        doubleBreakingBlock = null;
        breakDelay = breakDelaySet.getValue().intValue();
    }

    public boolean needAddToConveyor() {
        return currentBreakingBlock != null && (!doubleMine.getValue() || doubleBreakingBlock != null);
    }
}