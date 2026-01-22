package com.ferra13671.BThack.impl.modules.player;

import com.ferra13671.BThack.events.block.AttackBlockEvent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.InventoryUtils;
import com.ferra13671.BThack.api.utils.ItemUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "AutoTool", description = "lang.module.AutoTool", category = "PLAYER")
public class AutoTool extends Module {


    @EventSubscriber(priority = Integer.MIN_VALUE)
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onLeftClick(AttackBlockEvent e) {
        if (nullCheck() || e.isCancelled()) return;

        equipBestSlot(mc.world.getBlockState(e.getBlockPos()));
    }

    @SuppressWarnings("DataFlowIssue")
    public static void equipBestSlot(BlockState blockState) {
        double bestScore = -1;
        int bestSlot = -1;

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);

            double score = ItemUtils.getScore(itemStack, blockState, itemStack2 -> true);
            if (score < 0) continue;

            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }

        if (bestScore != -1) {
            if (bestSlot < 9)
                InventoryUtils.swapItem(bestSlot);
            else {
                int freeSlot = InventoryUtils.findFreeHotbarSlot();
                if (freeSlot != -1) {
                    InventoryUtils.swapItemOnInventory(freeSlot, bestSlot);
                    InventoryUtils.swapItem(freeSlot);
                    return;
                }
                InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, bestSlot);
                mc.interactionManager.tick();
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public static int getBestSlot(BlockState blockState, int slots) {
        MinecraftClient mc = MinecraftClient.getInstance();

        double bestScore = -1;
        int bestSlot = -1;

        for (int i = 0; i < slots; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);

            double score = ItemUtils.getScore(itemStack, blockState, itemStack2 -> true);
            if (score < 0) continue;

            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }

        if (bestScore != -1)
            return bestSlot;
        return -1;
    }
}
