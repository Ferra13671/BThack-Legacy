package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Interfaces.Pc;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

public final class InventoryUtils implements Mc, Pc {

    public static final int HEAD_SLOT = 5;
    public static final int CHESTPLATE_SLOT = 6;
    public static final int LEGS_SLOT = 7;
    public static final int FEET_SLOT = 8;
    public static final int OFFHAND_SLOT = 45;

    public static void swapItem(int needSlot) {
        mc.player.getInventory().selectedSlot = needSlot;
        pc.tick();
    }

    public static void packetSwapItem(int needSlot) {
        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(needSlot));
    }

    public static void swapItemOnInventory(int needHotbarSlot, int inventorySlot) {
        pc.tick();
        pc.clickSlot(0, inventorySlot, needHotbarSlot, SlotActionType.SWAP);
        pc.tick();
    }

    public static void packetSwapItemOnInventory(int needHotbarSlot, int inventorySlot) {
        pc.packetClickSlot(0, inventorySlot, needHotbarSlot, SlotActionType.SWAP);
    }

    public static int findItem(Item item) {
        return findItem(item, 36);
    }

    public static int findItem(Item item, int slots) {
        return findItem(item, slots, stack -> 1);
    }

    public static int findItem(Item item, int slots, ItemFilter filter) {
        int bestSlot = -1;
        int bestScore = -1;
        for (int i = 0; i < slots; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == item) {
                int score = filter.checkScore(stack);
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

    public static int findItem(Class<? extends Item> item, int slots, ItemFilter filter) {
        int bestSlot = -1;
        int bestScore = -1;
        for (int i = 0; i < slots; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem().getClass().equals(item)) {
                int score = filter.checkScore(stack);
                if (score > bestScore) {
                    bestSlot = i;
                    bestScore = score;
                }
            }
        }
        return bestSlot;
    }

    public static ItemStack getItem(int hotbarSlot) {
        return mc.player.getInventory().getStack(hotbarSlot);
    }

    public static int findFreeHotbarSlot() {
        for (int a = 0; a < 9; a++) {
            if (mc.player.getInventory().getStack(a).getItem() == Items.AIR) {
                return a;
            }
        }
        return -1;
    }

    public static void replaceItems(int slot1, int slot2, int delay) {
        ThreadManager.startNewThread(thread -> {
            pc.clickSlot(0, slot1, 0, SlotActionType.PICKUP);
            pc.tick();
            if (delay > 0) {
                try {
                    thread.sleep(delay);
                } catch (InterruptedException ignored) {}
            }
            pc.clickSlot(0, slot2, 0, SlotActionType.PICKUP);
            pc.tick();
            if (delay > 0) {
                try {
                    thread.sleep(delay);
                } catch (InterruptedException ignored) {}
            }
            pc.clickSlot(0, slot1, 0, SlotActionType.PICKUP);
            pc.tick();
        });
    }

    public static void replaceItems(int slot1, int slot2) {
        pc.clickSlot(0, slot1, 0, SlotActionType.PICKUP);
        pc.tick();
        pc.clickSlot(0, slot2, 0, SlotActionType.PICKUP);
        pc.tick();
        pc.clickSlot(0, slot1, 0, SlotActionType.PICKUP);
        pc.tick();
    }

    public static void swapAction(int oldSlot, int slot, boolean post, String swapMode) {
        if (!post && oldSlot == slot) return;

        switch (swapMode) {
            case "Client" -> {
                if (slot < 9) {
                    InventoryUtils.swapItem((post ? oldSlot : slot));
                } else {
                    InventoryUtils.swapItemOnInventory(oldSlot, slot);
                }
            }
            case "Packet" -> {
                if (slot < 9)
                    InventoryUtils.packetSwapItem((post ? oldSlot : slot));
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, slot);
            }
        }
    }

    public interface ItemFilter {
        int checkScore(ItemStack stack);
    }
}
