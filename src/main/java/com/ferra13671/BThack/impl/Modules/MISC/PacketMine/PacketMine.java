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
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketMine extends Module {

    public final ModeSetting page = new ModeSetting("Page", this, new ArrayList<>(Arrays.asList("General", "Render")));

    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, true, () -> page.getValue().equals("General"));

    public final BooleanSetting removeIfUse = new BooleanSetting("Remove If Use", this, true, () -> page.getValue().equals("General"));
    public final BooleanSetting stopPackets = new BooleanSetting("Stop Packets", this, true, () -> removeIfUse.getValue() && page.getValue().equals("General"));
    public final NumberSetting breakDelaySet = new NumberSetting("Break Delay", this, 5, 0, 5, true, () -> page.getValue().equals("General"));


    public final BooleanSetting conveyorMode = new BooleanSetting("Conveyor Mode", this, false, () -> page.getValue().equals("General"));
    public final BooleanSetting conveyorLimitState = new BooleanSetting("ConveyorLimit", this, false, () -> conveyorMode.getValue() && page.getValue().equals("General"));
    public final NumberSetting conveyorLimit = new NumberSetting("Limit", this, 2, 1, 10, true, () -> page.getValue().equals("General") && conveyorLimitState.getValue());

    public final BooleanSetting doubleMode = new BooleanSetting("Double Mode", this, false, () -> conveyorMode.getValue() && page.getValue().equals("General"));
    public final NumberSetting fastSpeed = new NumberSetting("Fast Speed", this, 5, 5, 50, false, () -> conveyorMode.getValue() && doubleMode.getValue() && page.getValue().equals("General"));
    public final NumberSetting normalSpeed = new NumberSetting("Normal Speed", this, 1.05, 0.9, 1.3, false, () -> conveyorMode.getValue() && doubleMode.getValue() && page.getValue().equals("General"));
    public final BooleanSetting switchToOld = new BooleanSetting("Switch To Old", this, false, () -> conveyorMode.getValue() && doubleMode.getValue() && page.getValue().equals("General"));

    public final BooleanSetting instaRebreak = new BooleanSetting("Insta Rebreak", this, false, () -> page.getValue().equals("General"));

    public final BooleanSetting speedMine = new BooleanSetting("Speed Mine", this, false, () -> !doubleMode.getValue() && page.getValue().equals("General"));
    public final NumberSetting mineSpeed = new NumberSetting("Mine Speed", this, 1.2, 1, 10, false, () -> speedMine.getValue() && !doubleMode.getValue() && page.getValue().equals("General"));

    public final BooleanSetting autoCityMode = new BooleanSetting("Auto City", this, false, () -> page.getValue().equals("General"));
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false, () -> autoCityMode.getValue() && page.getValue().equals("General"));

    public final BooleanSetting inventoryMode = new BooleanSetting("Inventory Mode", this, false, () -> page.getValue().equals("General"));
    public final NumberSetting hotbarSlot = new NumberSetting("Hotbar Slot", this, 1, 1, 9, true, () -> inventoryMode.getValue() && page.getValue().equals("General"));

    public final BooleanSetting renderBox = new BooleanSetting("Render Box", this, true, () -> page.getValue().equals("Render"));
    public final ColorSetting boxColor = new ColorSetting("Box Color", this, new Color(0, 255, 0), () -> renderBox.getValue() && page.getValue().equals("Render")).withBlockedAlpha();
    public final BooleanSetting conveyorRender = new BooleanSetting("Conveyor Render", this, true, () -> renderBox.getValue() && page.getValue().equals("Render"));

    public final NumberSetting conveyorAlpha = new NumberSetting("Conv. Alpha", this, 255, 0, 255, true, () -> renderBox.getValue() && conveyorRender.getValue() && page.getValue().equals("Render"));


    public PacketMine() {
        super("PacketMine",
                "lang.module.PacketMine",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                page,

                swingHand,

                removeIfUse,
                stopPackets,
                breakDelaySet,

                conveyorMode,
                conveyorLimitState,
                conveyorLimit,

                doubleMode,
                fastSpeed,
                normalSpeed,
                switchToOld,

                instaRebreak,

                speedMine,
                mineSpeed,

                autoCityMode,
                friends,

                inventoryMode,
                hotbarSlot,

                renderBox,
                boxColor,

                conveyorAlpha
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
    private boolean doubleFast;
    private BreakingBlock doubleBlock;
    private boolean firstSkip = true;

    //Inventory mode
    private int currentSlot = -1;
    private int currentHotbarSlot = -1;

    //Insta Rebreak
    BlockPos breakedPos;

    //Insta Rebreak Animation
    private final Animation instaRebreakAnimation = new Animation(Easing.LINEAR, 800);
    private boolean instaRebreakAnimationInvert = true;

    @Override
    public void onEnable() {
        super.onEnable();

        setCurrentBreakingBlock(null);
        conveyorBlocks.clear();
        currentSlot = -1;
        doubleFast = true;
        firstSkip = true;
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

        setCurrentBreakingBlock(null);
        conveyorBlocks.clear();
        if (!nullCheck())
            packetRemoveItem();
        doubleFast = true;
        firstSkip = true;
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
        updateBlockLimited(e.getBlockPos());
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
        if (e.blockHitResult.getBlockPos() == currentBreakingBlock.blockPos) {
            e.setCancelled(true);
            if (stopPackets.getValue())
                Managers.NETWORK_MANAGER.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, currentBreakingBlock.blockPos, RotateUtils.getInvertedFacingEntity(mc.player), 0));

            setCurrentBreakingBlock(null);
        }
    }
    public void updateBlockLimited(BlockPos pos) {  //Okay
        if (!conveyorLimitState.getValue()) {
            updateBlock(pos);
            return;
        }
        if (conveyorBlocks.size() >= conveyorLimit.getValue() + 1) {
            if (conveyorMode.getValue()) {
                conveyorBlocks.removeFirst();
            }
            if (pos == currentBreakingBlock.blockPos) {
                if (stopPackets.getValue())
                    Managers.NETWORK_MANAGER.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, currentBreakingBlock.blockPos, RotateUtils.getInvertedFacingEntity(mc.player), 0));
                setCurrentBreakingBlock(null);
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


        setCurrentBreakingBlock(new BreakingBlock(pos));
        return true;
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

        if (instaRebreak.getValue()) {
            if (breakedPos != null) {
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
        }

        if (renderBox.getValue() && currentBreakingBlock != null) {
            if (conveyorAnimation.getEase() >= 1) {
                conveyorAnimation.reset();
                conveyorAnimationInvert = !conveyorAnimationInvert;
            }

            double currentDestroyBlockSize = MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(true), currentBreakingBlock.prevDestroyProgress, currentBreakingBlock.currentDestroyProgress) / 2;
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
        }

        if (!renderBoxes.isEmpty()) {
            BThackRender.boxRender.prepareBoxRender();
            BThackRender.boxRender.renderBoxes(renderBoxes);
            BThackRender.boxRender.stopBoxRender();
        }
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
                updateBlockLimited(breakedPos);
                breakDelay = 0; //For InstaRebreak, the break delay doesn't make sense
            }
        }

        if ((!conveyorMode.getValue() || conveyorBlocks.isEmpty()) && currentBreakingBlock == null) doubleBlock = null;

        if (!conveyorMode.getValue())
            if (!conveyorBlocks.isEmpty())
                conveyorBlocks.clear();

        if (conveyorBlocks.isEmpty()) firstSkip = true;

        if (autoCityMode.getValue())
            autoCityAction();


        if (currentBreakingBlock == null) {
            doubleFast = true;
            destroyDelta = 0;
            return;
        }

        if (destroyDelta == 0)
            checkDestroyDelta();

        if (breakDelay > 0) {
            breakDelay--;
            return;
        }
        if (!updateBreak(currentBreakingBlock, true)) {

            packetRemoveItem();
            itemRemoved = false;

            if (conveyorMode.getValue()) {
                for (BreakingBlock breakingBlock : conveyorBlocks) {
                    if (breakingBlock.equals(currentBreakingBlock)) {
                        conveyorBlocks.remove(breakingBlock);
                        break;
                    }
                }
                if (!firstSkip) {
                    conveyorBlocks.removeFirst();
                } else {
                    firstSkip = false;
                }

                breakedPos = currentBreakingBlock.blockPos;
                setCurrentBreakingBlock(null);
                if (!conveyorBlocks.isEmpty())
                    updateBlock(conveyorBlocks.getFirst().blockPos);
            } else {
                breakedPos = currentBreakingBlock.blockPos;
                setCurrentBreakingBlock(null);
            }
        }
    }

    public boolean updateBreak(BreakingBlock breakingBlock, boolean reset) {
        if (mc.world.isAir(breakingBlock.blockPos) || !BlockUtils.canBreak(breakingBlock.blockPos)) {
            packetRemoveItem();

            if (reset) destroyDelta = 0;
            return false;
        }

        if (reset && currentSlot == -1) {
            packetEquipItem();
        }

        try {
            if (breakingBlock.currentDestroyProgress == 1) {

                if (instaRebreak.getValue() && breakedPos != null && breakedPos.equals(breakingBlock.blockPos)) return false;

                stopDestroyBlock(breakingBlock.blockPos);


                if (reset) destroyDelta = 0;
                doubleFast = !doubleFast;
                if (!doubleFast) {
                    doubleBlock = currentBreakingBlock;
                } else {
                    if (doubleBlock != null && conveyorMode.getValue() && doubleMode.getValue() && switchToOld.getValue()) {
                        if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
                        startDestroyBlock(doubleBlock.blockPos);
                        stopDestroyBlock(doubleBlock.blockPos);
                    }
                }
                return false;
            }
            if (!breakingBlock.startDestroying) {

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
                breakingBlock.currentDestroyProgress += getDestroyDelta();
                if (breakingBlock.currentDestroyProgress >= 1) breakingBlock.currentDestroyProgress = 1;
            }
        } catch (Exception ignored) {}

        if (breakingBlock.blockPos != null) {
            if (mc.world.isAir(breakingBlock.blockPos) || !BlockUtils.canBreak(breakingBlock.blockPos)) {
                if (reset) destroyDelta = 0;
                return false;
            }
        }
        return true;
    }

    private double getDestroyDelta() {
        return destroyDelta * (doubleMode.getValue() && conveyorMode.getValue() && !conveyorBlocks.isEmpty() ? (doubleFast && (firstSkip || conveyorBlocks.size() > 1) ? fastSpeed.getValue() : normalSpeed.getValue()) : (speedMine.getValue() ? mineSpeed.getValue() : 1));
    }

    private void startDestroyBlock(BlockPos blockPos) {
        Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, RotateUtils.getInvertedFacingEntity(mc.player), id));
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
                updateBlockLimited(blockPos);
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
            if (BlockUtils.canBreak(nearestPos)) updateBlockLimited(nearestPos);
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

    public void setCurrentBreakingBlock(BreakingBlock breakingBlock) {
        currentBreakingBlock = breakingBlock;
        breakDelay = breakDelaySet.getValue().intValue();
    }
}