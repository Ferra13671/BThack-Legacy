package com.ferra13671.BThack.api.utils;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.thread.ThreadManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

import java.util.function.Function;

public final class InventoryUtils implements Mc {

    public static final int HEAD_SLOT = 5;
    public static final int CHESTPLATE_SLOT = 6;
    public static final int LEGS_SLOT = 7;
    public static final int FEET_SLOT = 8;
    public static final int OFFHAND_SLOT = 45;

    @SuppressWarnings("DataFlowIssue")
    public static void swapItem(int needSlot) {
        mc.player.getInventory().selectedSlot = needSlot;
        mc.interactionManager.tick();
    }

    public static void packetSwapItem(int needSlot) {
        Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(needSlot));
    }

    @SuppressWarnings("DataFlowIssue")
    public static void swapItemOnInventory(int needHotbarSlot, int inventorySlot) {
        mc.interactionManager.tick();
        mc.interactionManager.clickSlot(0, inventorySlot, needHotbarSlot, SlotActionType.SWAP, mc.player);
        mc.interactionManager.tick();
    }

    public static int findItem(Item item) {
        return findItem(item, 36);
    }

    public static int findItem(Item item, int slots) {
        return findItem(item, slots, stack -> 1);
    }

    @SuppressWarnings("DataFlowIssue")
    public static int findItem(Item item, int slots, Function<ItemStack, Integer> filter) {
        int bestSlot = -1;
        int bestScore = -1;
        for (int i = 0; i < slots; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == item) {
                int score = filter.apply(stack);
                if (score > bestScore) {
                    bestSlot = i;
                    bestScore = score;
                }
            }
        }
        return bestSlot;
    }

    public static int findItem(Class<? extends Item> item) {
        return findItem(item, 36);
    }

    public static int findItem(Class<? extends Item> item, int slots) {
        return findItem(item, slots, stack -> 1);
    }

    @SuppressWarnings("DataFlowIssue")
    public static int findItem(Class<? extends Item> item, int slots, Function<ItemStack, Integer> filter) {
        int bestSlot = -1;
        int bestScore = -1;
        for (int i = 0; i < slots; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem().getClass().equals(item)) {
                int score = filter.apply(stack);
                if (score > bestScore) {
                    bestSlot = i;
                    bestScore = score;
                }
            }
        }
        return bestSlot;
    }

    @SuppressWarnings("DataFlowIssue")
    public static ItemStack getItem(int hotbarSlot) {
        return mc.player.getInventory().getStack(hotbarSlot);
    }

    @SuppressWarnings("DataFlowIssue")
    public static int findFreeHotbarSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isEmpty()) return i;
        }
        return -1;
    }

    @SuppressWarnings("DataFlowIssue")
    public static int findFreeSlot() {
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).isEmpty()) return i;
        }
        return -1;
    }

    @SuppressWarnings("DataFlowIssue")
    public static void replaceItems(int slot1, int slot2, int delay) {
        ThreadManager.startNewThread(thread -> {
            mc.interactionManager.clickSlot(0, slot1, 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.tick();
            if (delay > 0) {
                thread.sleepThread(delay);
            }
            mc.interactionManager.clickSlot(0, slot2, 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.tick();
            if (delay > 0) {
                thread.sleepThread(delay);
            }
            mc.interactionManager.clickSlot(0, slot1, 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.tick();
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public static void replaceItems(int slot1, int slot2) {
        mc.interactionManager.clickSlot(0, slot1, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.tick();
        mc.interactionManager.clickSlot(0, slot2, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.tick();
        mc.interactionManager.clickSlot(0, slot1, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.tick();
    }

    public static void swapAction(int oldSlot, int slot, boolean post, String swapMode) {
        if (!post && oldSlot == slot) return;

        switch (swapMode) {
            case "Client" -> {
                if (slot < 9)
                    InventoryUtils.swapItem((post ? oldSlot : slot));
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, slot);
            }
            case "Packet" -> {
                if (slot < 9)
                    InventoryUtils.packetSwapItem((post ? oldSlot : slot));
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, slot);
            }
        }
    }
}
