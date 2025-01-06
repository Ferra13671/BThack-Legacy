package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public final class ItemUtils implements Mc {
    public static void useItem(Hand hand, boolean swing) {
        if (swing)
            mc.player.swingHand(hand);
        mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(hand, 0));
    }

    public static void useItem(Item item, boolean swing) {
        int oldSlot = mc.player.getInventory().selectedSlot;
        int slot = InventoryUtils.findItem(item);
        if (slot == -1) return;
        if (slot < 9) {
            InventoryUtils.swapItem(slot);
        } else {
            InventoryUtils.swapItemOnInventory(oldSlot, slot);
        }
        useItem(Hand.MAIN_HAND, swing);
        if (slot < 9) {
            InventoryUtils.swapItem(oldSlot);
        } else {
            InventoryUtils.swapItemOnInventory(oldSlot, slot);
        }
    }

    public static void useItemOnBlock(Item item, BlockPos pos) {
        int oldSlot = mc.player.getInventory().selectedSlot;
        int slot = InventoryUtils.findItem(item);
        if (slot == -1) return;
        if (slot < 9) {
            InventoryUtils.swapItem(slot);
        } else {
            InventoryUtils.swapItemOnInventory(oldSlot, slot);
        }
        BlockHitResult bhr = BuildManager.getHitResult(pos);
        if (bhr != null) {
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
            //mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0));
        }
        if (slot < 9) {
            InventoryUtils.swapItem(oldSlot);
        } else {
            InventoryUtils.swapItemOnInventory(oldSlot, slot);
        }
    }

    public static Item getItemFromName(String nameOrId) {
        try {
            Item item = Registries.ITEM.getOrEmpty(new Identifier(nameOrId))
                    .orElse(null);
            if (item == null)
                item = Registries.ITEM.getOrEmpty(new Identifier("minecraft:" + nameOrId)).orElse(null);

            return item;
        } catch (InvalidIdentifierException e) {
            return null;
        }
    }

    public static void useItemOnBlock(BlockPos pos) {
        BlockHitResult bhr = BuildManager.getHitResult(pos);
        useItemOnBlock(bhr);
    }

    public static void useItemOnBlock(BlockHitResult bhr) {
        if (bhr != null) {
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
            //mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0));
        }
    }

    public static int getBlock() {
        for(int i = 0; i < 36; ++i) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem) {
                return i;
            }
        }
        return -1;
    }

    public static int getItemDurability(ItemStack itemStack) {
        return itemStack.getMaxDamage() - itemStack.getDamage();
    }

    public static int getItemMaxDurability(ItemStack itemStack) {
        return itemStack.getMaxDamage();
    }

    public static float getItemDurabilityInPercentages(ItemStack itemStack) {
        return 100f - (((float)itemStack.getDamage() / (float)itemStack.getMaxDamage()) * 100f);
    }

    public static double getScore(ItemStack itemStack, BlockState state, Predicate<ItemStack> good) {
        if (!good.test(itemStack) || !isTool(itemStack)) return -1;
        if (!itemStack.isSuitableFor(state) && !(itemStack.getItem() instanceof SwordItem && (state.getBlock() instanceof BambooBlock || state.getBlock() instanceof BambooShootBlock)) && !(itemStack.getItem() instanceof ShearsItem && state.getBlock() instanceof LeavesBlock || state.isIn(BlockTags.WOOL))) return -1;

        double score = 0;

        score += itemStack.getMiningSpeedMultiplier(state) * 1000;
        score += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, itemStack);
        score += EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack);
        score += EnchantmentHelper.getLevel(Enchantments.MENDING, itemStack);

        if (itemStack.getItem() instanceof SwordItem item && (state.getBlock() instanceof BambooBlock || state.getBlock() instanceof BambooShootBlock))
            score += 9000 + (item.getMaterial().getMiningLevel() * 1000);


        return score;
    }

    public static boolean isTool(Item item) {
        return item instanceof ToolItem || item instanceof ShearsItem;
    }

    public static boolean isTool(ItemStack itemStack) {
        return isTool(itemStack.getItem());
    }
}
