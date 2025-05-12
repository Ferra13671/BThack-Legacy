package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "AutoFarmland", description = "lang.module.AutoFarmland", category = "MISC")
public class AutoFarmland extends Module {

    public final NumberSetting range = new NumberSetting("Range", this, 4, 3, 7, false);
    public final BooleanSetting checkWater = new BooleanSetting("Check Water", this, false);
    public final ModeSetting swap = new ModeSetting("Swap", this, Arrays.asList("Client", "Packet"));
    public final ModeSetting interact = new ModeSetting("Interact", this, Arrays.asList("Client", "Packet"));
    public final BooleanSetting rotate = new BooleanSetting("Rotate", this, false);
    public final ModeSetting rotateMode = new ModeSetting("Rotate Mode", this, Arrays.asList("Packet", "Grim"), rotate::getValue);
    public final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, true);
    public final NumberSetting tickDelay = new NumberSetting("Tick Delay", this, 3, 1, 20, true);


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

        for (BlockPos pos : BlockUtils.getSphere(mc.player.getBlockPos(), range.getValue().floatValue(), range.getValue().floatValue(), false, true, 0)) {
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
            float[] rots = RotateUtils.rotations(pos);
            if (rotateMode.getValue().equals("Packet"))
                RotateUtils.packetRotate(rots[0], rots[1]);
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


        for (RegistryEntry<Enchantment> ench : stack.getEnchantments().getEnchantments()) {
            int lvl = stack.getEnchantments().getLevel(ench);

            if (ench.equals(mc.world.getRegistryManager().get(Enchantments.UNBREAKING.getRegistryRef()).getEntry(Enchantments.UNBREAKING).get()))
                score += lvl;

        }

        return score;
    }
}
