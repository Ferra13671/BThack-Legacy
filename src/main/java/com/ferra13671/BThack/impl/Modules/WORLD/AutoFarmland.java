package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoFarmland extends Module {

    public static NumberSetting range;
    public static BooleanSetting checkWater;
    public static BooleanSetting swapLogic;
    public static ModeSetting swap;
    public static ModeSetting interact;
    public static BooleanSetting rotate;
    public static ModeSetting rotateMode;
    public static BooleanSetting ignoreWalls;
    public static NumberSetting tickDelay;

    public AutoFarmland() {
        super("AutoFarmland",
                "lang.module.AutoFarmland",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        range = new NumberSetting("Range", this, 4, 3, 7, false);
        checkWater = new BooleanSetting("Check Water", this, false);
        swapLogic = new BooleanSetting("Swap Logic", this, true);
        swap = new ModeSetting("Swap", this, Arrays.asList("Client", "Packet"));
        interact = new ModeSetting("Interact", this, Arrays.asList("Client", "Packet"));
        rotate = new BooleanSetting("Rotate", this, false);
        rotateMode = new ModeSetting("Rotate Mode", this, Arrays.asList("Packet", "Grim"), rotate::getValue);
        ignoreWalls = new BooleanSetting("Ignore Walls", this, true);
        tickDelay = new NumberSetting("Tick Delay", this, 3, 1, 20, true);

        initSettings(
                range,
                checkWater,
                swapLogic,
                swap,
                interact,
                rotate,
                rotateMode,
                ignoreWalls,
                tickDelay
        );
    }

    private final List<BlockPos> poses = new ArrayList<>();
    private final Ticker delayTicker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();
        delayTicker.reset();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (delayTicker.passed(tickDelay.getValue() * 50)) {
            filterAction();
            interactAction();
            delayTicker.reset();
        }
    }

    public void filterAction() {
        poses.clear();

        for (BlockPos pos : BlockUtils.getSphere(mc.player.getBlockPos(), (float) range.getValue(), (float) range.getValue(), false, true, 0)) {
            if (checkWater.getValue()) {
                if (mc.world.getBlockState(pos).getBlock() == Blocks.WATER)
                    waterDirtFilter(pos);
            } else
                dirtFilter(pos);
        }
    }

    public void waterDirtFilter(BlockPos pos) {
        for (int i = -4; i < 5; i++)
            for (int i2 = -4; i2 < 5; i2++)
                dirtFilter(pos);
    }

    public void dirtFilter(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.DIRT || block == Blocks.GRASS_BLOCK) {
            if (!ignoreWalls.getValue()) {
                if (!BlockUtils.hasLineOfSight(mc.player.getPos(), pos.toCenterPos())) return;
            }
            if (mc.world.isAir(pos.add(0, 1, 0)))
                poses.add(pos);
        }
    }

    public void interactAction() {
        for (BlockPos pos : poses) {
            int oldSlot = mc.player.getInventory().selectedSlot;
            int slot = findBestTool();

            InventoryUtils.swapAction(oldSlot, slot, false, swap.getValue());
            interactActionInternal(pos);
            InventoryUtils.swapAction(oldSlot, slot, true, swap.getValue());
        }
        poses.clear();
    }

    public void interactActionInternal(BlockPos pos) {
        rotatePreAction(pos);
        switch (interact.getValue()) {
            case "Client" -> mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, BuildManager.getHitResult(pos, false, Direction.UP));
            case "Packet" -> Managers.NETWORK_MANAGER.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, BuildManager.getHitResult(pos, false, Direction.UP), 0));
        }
        rotatePostAction();
    }

    public void rotatePreAction(BlockPos pos) {
        if (rotate.getValue()) {
            float[] rots = AimBotUtils.rotations(pos);
            if (rotateMode.getValue().equals("Packet"))
                AimBotUtils.packetRotate(rots[0], rots[1]);
            else
                GrimUtils.sendPreActionGrimPackets(rots[0], rots[1]);
        }
    }

    public void rotatePostAction() {
        if (rotate.getValue())
            if (rotateMode.getValue().equals("Grim"))
                GrimUtils.sendPostActionGrimPackets();
    }

    public int findBestTool() {
        int bestSlot;
        bestSlot = findBestToolInternal(Items.NETHERITE_HOE);
        if (bestSlot == -1) {
            bestSlot = findBestToolInternal(Items.DIAMOND_HOE);
            if (bestSlot == -1) {
                bestSlot = findBestToolInternal(Items.IRON_HOE);
                if (bestSlot == -1) {
                    bestSlot = findBestToolInternal(Items.GOLDEN_HOE);
                    if (bestSlot == -1) {
                        bestSlot = findBestToolInternal(Items.STONE_HOE);
                        if (bestSlot == -1) {
                            bestSlot = findBestToolInternal(Items.WOODEN_HOE);
                        }
                    }
                }
            }
        }

        return bestSlot;
    }

    public int findBestToolInternal(Item item) {
        return InventoryUtils.findItem(item, 36, stack -> {
            if (ItemUtils.getItemDurability(stack) < 30) return -999;
            return filterEnchantments(stack);
        });
    }

    public int filterEnchantments(ItemStack stack) {
        int score = 0;

        NbtList list = stack.getEnchantments();
        for (int i = 0; i < list.size(); i++) {
            String enchName = list.getCompound(i).getString("id");
            short lvl = list.getCompound(i).getShort("lvl");

            if ((enchName.equals("minecraft:unbreaking") || enchName.equals("unbreaking"))) {
                score += lvl;
            }

        }

        return score;
    }
}
