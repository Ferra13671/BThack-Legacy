package com.ferra13671.BThack.impl.Modules.Misc.PacketMine;

import com.ferra13671.BThack.managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.events.Block.AttackBlockEvent;
import com.ferra13671.BThack.events.Block.UseBlockEvent;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Break.BreakManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.BThack.impl.Modules.Player.AutoTool;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "PacketMine", description = "lang.module.PacketMine", category = "MISC")
public class PacketMine extends Module {

    //----------General----------//
    public final CategorySetting generalCategory = new CategorySetting("General", this);
    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, true).inCategory(generalCategory);
    public final NumberSetting breakDelaySet = new NumberSetting("Break Delay", this, 0, 0, 5, true).inCategory(generalCategory);

    public final BooleanSetting conveyorMode = new BooleanSetting("Conveyor Mode", this, false).inCategory(generalCategory);
    public final BooleanSetting removeIfUse = new BooleanSetting("Remove If Use", this, true, conveyorMode::getValue).inCategory(generalCategory);

    public final BooleanSetting doubleMine = new BooleanSetting("Double Mine", this, false).inCategory(generalCategory);
    public final NumberSetting doubleSpeed = new NumberSetting("Double Speed", this, 1, 0.5, 1, false, doubleMine::getValue).inCategory(generalCategory);

    public final BooleanSetting instaRebreak = new BooleanSetting("Insta Rebreak", this, false).inCategory(generalCategory);

    public final BooleanSetting speedMine = new BooleanSetting("Speed Mine", this, false, () -> !doubleMine.getValue()).inCategory(generalCategory);
    public final NumberSetting mineSpeed = new NumberSetting("Mine Speed", this, 1.2, 1, 10, false, () -> speedMine.getValue() && !doubleMine.getValue()).inCategory(generalCategory);

    public final BooleanSetting autoCityMode = new BooleanSetting("Auto City", this, false).inCategory(generalCategory);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false, autoCityMode::getValue).inCategory(generalCategory);

    public final BooleanSetting inventoryMode = new BooleanSetting("Inventory Mode", this, false).inCategory(generalCategory);
    public final NumberSetting hotbarSlot = new NumberSetting("Hotbar Slot", this, 1, 1, 9, true, inventoryMode::getValue).inCategory(generalCategory);
    //---------------------------//


    //----------Render----------//
    public final CategorySetting renderCategory = new CategorySetting("Render", this);
    public final ModeSetting boxMode = new ModeSetting("Box Mode", this, Arrays.asList("Static Color", "Progress Color")).inCategory(renderCategory);
    public final ColorSetting boxColor = new ColorSetting("Box Color", this, new Color(0, 255, 0), () -> boxMode.getValue().equals("Static Color")).withBlockedAlpha().inCategory(renderCategory);
    public final ColorSetting startBoxColor = new ColorSetting("Start Color", this, new Color(255, 0, 0), () -> boxMode.getValue().equals("Progress Color")).withBlockedAlpha().inCategory(renderCategory);
    public final ColorSetting endBoxColor = new ColorSetting("End Color", this, new Color(0, 255, 0), () -> boxMode.getValue().equals("Progress Color")).withBlockedAlpha().inCategory(renderCategory);

    public final CategorySetting instaRebreakCategory = new CategorySetting("Insta Rebreak", this).inCategory(renderCategory);
    public final ModeSetting instaRebreakAnimationMode = new ModeSetting("Anim. Mode", this, Arrays.asList("Static", "Color", "Size")).defaultValue("Color").inCategory(instaRebreakCategory);
    public final NumberSetting instaRebreakBoxSize = new NumberSetting("Box Size", this, 1, 0.1, 1, false).inCategory(instaRebreakCategory);
    public final ColorSetting instaRebreakColor = new ColorSetting("Insta Rebreak Color", this, new Color(255, 0, 0)).withBlockedAlpha().inCategory(instaRebreakCategory);
    public final NumberSetting instaRebreakAnimTime = new NumberSetting("Anim. Time", this, 1000, 500, 3000, true).inCategory(instaRebreakCategory);

    public final CategorySetting conveyorCategory = new CategorySetting("Conveyor", this).inCategory(renderCategory);
    public final ModeSetting conveyorAnimationMode = new ModeSetting("Anim. Mode", this, Arrays.asList("Static", "Color", "Size")).defaultValue("Color").inCategory(conveyorCategory);
    public final NumberSetting conveyorBoxSize = new NumberSetting("Box Size", this, 0.2, 0.1, 1, false).inCategory(conveyorCategory);
    public final ColorSetting conveyorColor = new ColorSetting("Conveyor Color", this, new Color(255, 255, 0, 255)).inCategory(conveyorCategory);
    public final NumberSetting conveyorAnimTime = new NumberSetting("Anim. Time", this, 1000, 500, 3000, true).inCategory(conveyorCategory);
    //--------------------------//


    private final List<Vec3i> autoCityVectors = Arrays.asList(
            new Vec3i(1,0,0),
            new Vec3i(0,0,1),
            new Vec3i(-1,0,0),
            new Vec3i(0,0,-1)
    );

    //Basic variables
    public BreakingBlock currentBreakingBlock;
    private float destroyDelta = 0;
    private int breakDelay = 5;

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
    private BlockPos breakedPos;

    //Insta Rebreak Animation
    private final Animation instaRebreakAnimation = new Animation(Easing.LINEAR, 1000);
    private boolean instaRebreakAnimationInvert = true;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (setting == conveyorAnimTime) conveyorAnimation.setMillis(conveyorAnimTime.getValue().intValue());
        if (setting == instaRebreakAnimTime) instaRebreakAnimation.setMillis(instaRebreakAnimTime.getValue().intValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();

        clearBreakBlocks();
        conveyorBlocks.clear();
        currentSlot = -1;

        ModuleList.superInstaMine.setEnabled(false);

        conveyorAnimation.reset();
        conveyorAnimation.setMillis(conveyorAnimTime.getValue().intValue());
        instaRebreakAnimation.setMillis(instaRebreakAnimTime.getValue().intValue());
        conveyorAnimationInvert = true;

        instaRebreakAnimation.reset();
        instaRebreakAnimationInvert = true;

        breakedPos = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        clearBreakBlocks();
        conveyorBlocks.clear();
        if (!nullCheck())
            packetRemoveItem();

        ModuleList.treeCutter.setEnabled(false);
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    @SuppressWarnings("unused")
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck() || BreakManager.isDestroying) return;
        if (e.getBlockPos() == null) return;

        e.setCancelled(true);
        if (!BlockUtils.canBreak(e.getBlockPos())) return;
        if (currentBreakingBlock != null && currentBreakingBlock.blockPos.equals(e.getBlockPos())) return;
        updateBlock(e.getBlockPos());
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    @SuppressWarnings("unused")
    public void onUseBlock(UseBlockEvent e) {
        if (e.blockHitResult.getBlockPos() == null) return;
        if (currentBreakingBlock == null) return;
        if (!removeIfUse.getValue()) return;
        if (conveyorMode.getValue() && conveyorContains(e.getBlockHitResult().getBlockPos())) {
            conveyorBlocks.removeIf(breakingBlock -> breakingBlock.blockPos.equals(e.getBlockHitResult().getBlockPos()));
            e.setCancelled(true);
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onRender(RenderWorldLastEvent e) {
        ArrayList<RenderBox> renderBoxes = new ArrayList<>();

        if (instaRebreakAnimation.getEase() >= 1) {
            instaRebreakAnimation.reset();
            instaRebreakAnimationInvert = !instaRebreakAnimationInvert;
        }

        if (instaRebreak.getValue() && breakedPos != null) {
            float animStep = (float) (instaRebreakAnimationInvert ? 1 - instaRebreakAnimation.getEase() : instaRebreakAnimation.getEase());
            float[] color = new float[]{instaRebreakColor.getValue().getRed() / 255f, instaRebreakColor.getValue().getGreen() / 255f, instaRebreakColor.getValue().getBlue() / 255f, 1f};
            float boxSize = instaRebreakBoxSize.getValue().floatValue() / 2f;
            switch (instaRebreakAnimationMode.getValue()) {
                case "Size" -> boxSize *= animStep;
                case "Color" -> {
                    color[0] *= animStep;
                    color[1] *= animStep;
                    color[2] *= animStep;
                    color[3] *= animStep;
                }
            }
            renderBoxes.add(new RenderBox(
                    BlockUtils.createBox(breakedPos, boxSize, boxSize, boxSize, true),
                    color[0],
                    color[1],
                    color[2],
                    color[3],
                    color[0],
                    color[1],
                    color[2],
                    color[3] * 0.3f));
        }

        if (getAnyBreakingBlock() != null) {
            if (currentBreakingBlock != null)
                renderBoxes.add(getRenderBox(currentBreakingBlock));

            if (doubleMine.getValue() && doubleBreakingBlock != null)
                renderBoxes.add(getRenderBox(doubleBreakingBlock));

            if (conveyorMode.getValue()) {
                if (conveyorAnimation.getEase() >= 1) {
                    conveyorAnimation.reset();
                    conveyorAnimationInvert = !conveyorAnimationInvert;
                }

                float animStep = (float) (conveyorAnimationInvert ? 1 - conveyorAnimation.getEase() : conveyorAnimation.getEase());
                float[] convColor = new float[]{conveyorColor.getValue().getRed() / 255f, conveyorColor.getValue().getGreen() / 255f, conveyorColor.getValue().getBlue() / 255f, conveyorColor.getValue().getAlpha() / 255f};
                float convBoxSize = conveyorBoxSize.getValue().floatValue() / 2f;
                switch (conveyorAnimationMode.getValue()) {
                    case "Size" -> convBoxSize *= animStep;
                    case "Color" -> {
                        convColor[0] *= animStep;
                        convColor[1] *= animStep;
                        convColor[2] *= animStep;
                        convColor[3] *= animStep;
                    }
                }
                for (BreakingBlock pos : conveyorBlocks) {
                    renderBoxes.add(
                            new RenderBox(BlockUtils.createBox(
                                            pos.blockPos,
                                            convBoxSize,
                                            convBoxSize,
                                            convBoxSize,
                                            true
                                    ),
                                    convColor[0],
                                    convColor[1],
                                    convColor[2],
                                    convColor[3],
                                    convColor[0],
                                    convColor[1],
                                    convColor[2],
                                    0.3f * convColor[3]
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

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || mc.isPaused()) return;

        if (instaRebreak.getValue() && breakedPos != null) {
            if (BlockUtils.canBreak(breakedPos) && currentBreakingBlock == null) {
                updateBlock(breakedPos);
                breakDelay = 0; //For InstaRebreak, the break delay doesn't make sense
            }
        }

        if (!conveyorMode.getValue() && !conveyorBlocks.isEmpty())
            conveyorBlocks.clear();

        if (autoCityMode.getValue())
            autoCityAction();


        if (getAnyBreakingBlock() == null) {
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

            clearBreakBlocks();
            if (conveyorMode.getValue()) {
                conveyorBlocks.removeIf(breakingBlock -> breakingBlock.equals(currentBreakingBlock) || breakingBlock.equals(doubleBreakingBlock));

                if (!conveyorBlocks.isEmpty()) {
                    updateBlock(conveyorBlocks.getFirst().blockPos);
                    conveyorBlocks.removeFirst();
                    if (doubleMine.getValue() && !conveyorBlocks.isEmpty()) {
                        updateBlock(conveyorBlocks.getFirst().blockPos);
                        conveyorBlocks.removeFirst();
                    }
                }
            }
        }
    }

    private RenderBox getRenderBox(BreakingBlock breakingBlock) {
        double blockSize = MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(true), breakingBlock.prevDestroyProgress, breakingBlock.currentDestroyProgress) / 2;
        float boxR = (float) (boxMode.getValue().equals("Static Color") ? boxColor.getValue().getRed() / 255f : MathHelper.lerp(breakingBlock.currentDestroyProgress, startBoxColor.getValue().getRed(), endBoxColor.getValue().getRed()) / 255f);
        float boxG = (float) (boxMode.getValue().equals("Static Color") ? boxColor.getValue().getGreen() / 255f : MathHelper.lerp(breakingBlock.currentDestroyProgress, startBoxColor.getValue().getGreen(), endBoxColor.getValue().getGreen()) / 255f);
        float boxB = (float) (boxMode.getValue().equals("Static Color") ? boxColor.getValue().getBlue() / 255f : MathHelper.lerp(breakingBlock.currentDestroyProgress, startBoxColor.getValue().getBlue(), endBoxColor.getValue().getBlue()) / 255f);
        return new RenderBox(
                BlockUtils.createBox(
                        breakingBlock.blockPos,
                        blockSize,
                        blockSize,
                        blockSize,
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
        );
    }

    public void updateBlock(BlockPos pos) {
        if (conveyorMode.getValue() && needAddToConveyor()) {
            if (!conveyorContains(pos))
                conveyorBlocks.add(new BreakingBlock(pos));
            return;
        }

        if (doubleMine.getValue() && currentBreakingBlock != null)
            doubleBreakingBlock = currentBreakingBlock;
        currentBreakingBlock = new BreakingBlock(pos);
    }

    @SuppressWarnings("DataFlowIssue")
    private void checkDestroyDelta() {
        BreakingBlock breakingBlock = getAnyBreakingBlock();
        int bestSlot = AutoTool.getBestSlot(mc.world.getBlockState(breakingBlock.blockPos), inventoryMode.getValue() ? 36 : 9);
        ItemStack stack = mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot);
        if (bestSlot != -1)
            stack = mc.player.getInventory().getStack(bestSlot);

        destroyDelta = ItemUtils.getMineSpeed(mc.world.getBlockState(breakingBlock.blockPos), breakingBlock.blockPos, stack);
    }

    public boolean updateBreakProgress() {
        if (getAnyBreakingBlock() == null) return false;
        if ((currentBreakingBlock == null || !currentBreakingBlock.canBreak()) && (doubleBreakingBlock == null || (!doubleMine.getValue() || !doubleBreakingBlock.canBreak()))) {
            packetRemoveItem();
            destroyDelta = 0;
            return false;
        }
        if (currentSlot == -1) packetEquipItem();

        boolean currentStop = checkStopAction(currentBreakingBlock);
        boolean doubleStop = !doubleMine.getValue() || checkStopAction(doubleBreakingBlock);
        if (currentStop && doubleStop) return false;

        updateBreakProgressInternal(currentBreakingBlock);
        if (doubleMine.getValue())
            updateBreakProgressInternal(doubleBreakingBlock);

        if (currentBreakingBlock != null && !currentBreakingBlock.canBreak()) {
            breakedPos = currentBreakingBlock.blockPos;
            currentBreakingBlock = null;
        }
        if (doubleBreakingBlock != null && !doubleBreakingBlock.canBreak())
            doubleBreakingBlock = null;
        return true;
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean checkStopAction(BreakingBlock breakingBlock) {
        if (breakingBlock == null) return true;
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

    @SuppressWarnings("DataFlowIssue")
    public void updateBreakProgressInternal(BreakingBlock breakingBlock) {
        if (breakingBlock == null) return;
        if (!breakingBlock.startDestroying) {
            if (breakingBlock == currentBreakingBlock)
                if (doubleMine.getValue() && doubleBreakingBlock != null && !doubleBreakingBlock.startDestroying) return;
            if (instaRebreak.getValue() && breakedPos != null && breakedPos.equals(breakingBlock.blockPos)){
                breakingBlock.currentDestroyProgress = 1;
                stopDestroyBlock(breakingBlock.blockPos);
            } else
                startDestroyBlock(breakingBlock.blockPos);
            if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
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

    @SuppressWarnings("DataFlowIssue")
    private void autoCityAction() {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player ||
                    (!friends.getValue() && Managers.FRIENDS_MANAGER.contains(player)) ||
                    player.distanceTo(mc.player) > 4
            ) continue;
            BlockPos blockPos = BlockPos.ofFloored(player.getX(), player.getY(),player.getZ());
            if (BlockUtils.canBreak(blockPos) && MathUtils.getDistance(mc.player.getPos(), blockPos.toCenterPos()) < 4.25)
                updateBlock(blockPos);

            BlockPos nearestPos = getNearestAutoCityBlockPos(player);
            if (nearestPos != null && BlockUtils.canBreak(nearestPos)) updateBlock(nearestPos);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private BlockPos getNearestAutoCityBlockPos(PlayerEntity player) {
        BlockPos nearestPos = null;
        double nearestLength = -1;
        for (Vec3i vec : autoCityVectors) {
            BlockPos checkPos = BlockPos.ofFloored(player.getX() + vec.getX(), player.getY(), player.getZ() + vec.getZ());
            double length = MathUtils.getDistance(mc.player.getPos(), checkPos.toCenterPos());
            if (length > 4.25) continue;
            if (length < nearestLength || nearestLength < 0) {
                nearestLength = length;
                nearestPos = checkPos;
            }
        }
        return nearestPos;
    }

    @SuppressWarnings("DataFlowIssue")
    private void packetEquipItem() {
        BreakingBlock breakingBlock = getAnyBreakingBlock();
        int bestSlot = AutoTool.getBestSlot(mc.world.getBlockState(breakingBlock.blockPos), inventoryMode.getValue() ? 36 : 9);
        if (bestSlot != -1) {
            if (bestSlot < 9) {
                Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(bestSlot));
            } else {
                currentHotbarSlot = hotbarSlot.getValue().intValue() - 1;
                mc.interactionManager.clickSlot(0, bestSlot, currentHotbarSlot, SlotActionType.SWAP, mc.player);
                currentSlot = bestSlot;
                Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(currentHotbarSlot));
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private void packetRemoveItem() {
        if (currentSlot != -1 && currentHotbarSlot != -1) {
            mc.interactionManager.clickSlot(0, currentSlot, currentHotbarSlot, SlotActionType.SWAP, mc.player);
            currentSlot = -1;
            currentHotbarSlot = -1;
        }
        Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
    }

    public boolean conveyorContains(BlockPos pos) {
        for (BreakingBlock breakingBlock : conveyorBlocks)
            if (breakingBlock.blockPos.equals(pos))
                return true;
        return false;
    }

    public void clearBreakBlocks() {
        currentBreakingBlock = null;
        doubleBreakingBlock = null;
        breakDelay = breakDelaySet.getValue().intValue();
    }

    public boolean needAddToConveyor() {
        return currentBreakingBlock != null && (!doubleMine.getValue() || doubleBreakingBlock != null);
    }

    public BreakingBlock getAnyBreakingBlock() {
        return (currentBreakingBlock == null && doubleMine.getValue()) ? doubleBreakingBlock : currentBreakingBlock;
    }
}