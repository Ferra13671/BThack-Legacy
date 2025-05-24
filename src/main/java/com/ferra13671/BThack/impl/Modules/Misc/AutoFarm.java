package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.IMixin.ModifyClientPlayerInteractionManager;
import com.ferra13671.BThack.managers.managers.Place.PlaceManager;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.GrimUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.HashMap;

@ModuleInfo(name = "AutoFarm", description = "lang.module.AutoFarm", category = "MISC")
public class AutoFarm extends Module {

    public final BooleanSetting rotate = new BooleanSetting("Rotate", this, false);
    public final ModeSetting swap = new ModeSetting("Swap", this, Arrays.asList("Packet", "Client"));
    public final BooleanSetting fortuneFilter = new BooleanSetting("Fortune Filter", this, true);
    public final BooleanSetting _break = new BooleanSetting("Break", this, true);
    public final BooleanSetting plant = new BooleanSetting("Plant", this, true);
    public final BooleanSetting logicPlant = new BooleanSetting("Logic Plant", this, true, plant::getValue);
    public final ModeSetting plantCrop = new ModeSetting("Plant Crop", this, Arrays.asList("Wheat", "Potato", "Carrot", "Beetroot"), plant::getValue);


    private HashMap<BlockPos, CropBlock> breakPoses = new HashMap<>();
    private HashMap<BlockPos, Item> prevBreakPoses = new HashMap<>();

    private HashMap<BlockPos, Item> plantPoses = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();
        breakPoses.clear();
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (_break.getValue()) {
            breakFilterAction();
            breakAction();
        }
        if (plant.getValue()) {
            plantFilterAction();
            plantAction();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void breakFilterAction() {
        breakPoses = new HashMap<>();
        for (BlockPos pos : BlockUtils.getSphere(mc.player.getBlockPos(), 4, 4, false, true, 0)) {
            BlockState state = mc.world.getBlockState(pos);
            if (state.getBlock() instanceof CropBlock block) {
                if (block.getAge(state) >= block.getMaxAge()) breakPoses.put(pos, block);
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void breakAction() {
        prevBreakPoses = new HashMap<>();
        breakPoses.forEach((pos, crop) -> {
            float[] rotations = RotateUtils.rotations(pos.toCenterPos());
            GrimUtils.sendPreActionGrimPackets(rotations[0], rotations[1]);
            int slot = -1;
            int prevSlot = -1;
            if (fortuneFilter.getValue()) slot = findBestItem();
            if (slot != -1) {
                prevSlot = mc.player.getInventory().selectedSlot;
                InventoryUtils.swapAction(prevSlot, slot, false, swap.getValue());
            }
            ((ModifyClientPlayerInteractionManager) mc.interactionManager).attackBlockNoEvent(pos, Direction.UP);
            mc.world.breakBlock(pos, false);
            if (slot != -1)
                InventoryUtils.swapAction(prevSlot, slot, true, swap.getValue());

            prevBreakPoses.put(pos, crop.getPickStack(mc.world, pos, mc.world.getBlockState(pos), true).getItem());
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void plantFilterAction() {
        plantPoses = new HashMap<>();
        for (BlockPos pos : BlockUtils.getSphere(mc.player.getBlockPos(), 4, 4, false, true, 0)) {
            BlockState state = mc.world.getBlockState(pos);
            if (state.getBlock() instanceof FarmlandBlock) {
                BlockPos upPos = pos.add(0, 1, 0);
                if (!mc.world.isAir(upPos)) continue;
                if (logicPlant.getValue()) {
                    if (prevBreakPoses.containsKey(upPos)) {
                        plantPoses.put(upPos, prevBreakPoses.get(upPos));
                        continue;
                    }
                }
                plantPoses.put(upPos, getSeedItem());
            }
        }
        prevBreakPoses.clear();
    }

    @SuppressWarnings("DataFlowIssue")
    public void plantAction() {
        plantPoses.forEach((pos, seed) -> {
            int slot = InventoryUtils.findItem(seed);
            if (mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot).getItem() == seed) slot = mc.player.getInventory().selectedSlot;
            if (slot != -1) {
                int oldSlot = mc.player.getInventory().selectedSlot;
                if (oldSlot != slot)
                    InventoryUtils.swapAction(oldSlot, slot, false, swap.getValue());
                float[] rotations = RotateUtils.rotations(pos.toCenterPos());
                GrimUtils.sendPreActionGrimPackets(rotations[0], rotations[1]);
                ItemUtils.useItemOnBlock(PlaceManager.getHitResult(pos, false, Direction.UP));
                GrimUtils.sendPostActionGrimPackets();
                if (oldSlot != slot)
                    InventoryUtils.swapAction(oldSlot, slot, true, swap.getValue());
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public int findBestItem() {
        double bestScore = -1;
        int bestSlot = -1;

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);

            double score = ItemUtils.getEnchantmentLevel(itemStack, Enchantments.FORTUNE);
            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }
        return bestSlot;
    }

    public Item getSeedItem() {
        return switch (plantCrop.getValue()) {
            case "Wheat" -> Items.WHEAT_SEEDS;
            case "Potato" -> Items.POTATO;
            case "Carrot" -> Items.CARROT;
            case "Beetroot" -> Items.BEETROOT_SEEDS;
            default -> null;
        };
    }
}
