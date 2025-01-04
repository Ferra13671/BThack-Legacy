package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import net.minecraft.block.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.Objects;
import java.util.function.Predicate;

public final class ItemUtils implements Mc {
    public static void useItem(Hand hand, boolean swing, float yaw, float pitch) {
        if (swing)
            mc.player.swingHand(hand);
        mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(hand, 0, yaw, pitch));
    }

    public static void useItem(Item item, boolean swing, float yaw, float pitch) {
        int oldSlot = mc.player.getInventory().selectedSlot;
        int slot = InventoryUtils.findItem(item);
        if (slot == -1) return;
        if (slot < 9) {
            InventoryUtils.swapItem(slot);
        } else {
            InventoryUtils.swapItemOnInventory(oldSlot, slot);
        }
        useItem(Hand.MAIN_HAND, swing, yaw, pitch);
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
        if(MathUtils.isInteger(nameOrId)) {
            int id = Integer.parseInt(nameOrId);
            Item item = Registries.ITEM.get(id);
            if(id != 0 && Registries.ITEM.getRawId(item) == 0)
                return null;

            return item;
        }

        try {
            return Registries.ITEM.getOrEmpty(Identifier.of(nameOrId))
                    .orElse(null);

        } catch(InvalidIdentifierException e) {
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
        DynamicRegistryManager dynamicRegistryManager = mc.world.getRegistryManager();
        Registry<Enchantment> enchs = dynamicRegistryManager.get(RegistryKeys.ENCHANTMENT);
        score += enchs.getEntry(Enchantments.UNBREAKING).map(entry -> EnchantmentHelper.getLevel(entry, itemStack)).orElse(0);
        score += enchs.getEntry(Enchantments.EFFICIENCY).map(entry -> EnchantmentHelper.getLevel(entry, itemStack)).orElse(0);
        score += enchs.getEntry(Enchantments.MENDING).map(entry -> EnchantmentHelper.getLevel(entry, itemStack)).orElse(0);

        if (itemStack.getItem() instanceof SwordItem item && (state.getBlock() instanceof BambooBlock || state.getBlock() instanceof BambooShootBlock))
            score += 9000 + (item.getMaterial().getDurability());


        return score;
    }

    public static float getMineSpeed(BlockState state, BlockPos position, ItemStack itemStack) {
        if (state == Blocks.AIR.getDefaultState())
            return 0.02f;

        float hardness = state.getHardness(mc.world, position);

        if (hardness < 0)
            return 0;

        return getMineSpeedInternal(state, itemStack) / hardness / (BlockUtils.canBreak(position) ? 30f : 100f);
    }

    private static float getMineSpeedInternal(BlockState state, ItemStack itemStack) {
        if (mc.player == null) return 0;
        float digSpeed = getDestroySpeed(state, itemStack);

        if (digSpeed > 1) {
            int efficiencyModifier = EnchantmentHelper.getLevel(mc.world.getRegistryManager().get(Enchantments.EFFICIENCY.getRegistryRef()).getEntry(Enchantments.EFFICIENCY).get(), itemStack);
            if (efficiencyModifier > 0 && !itemStack.isEmpty()) {
                digSpeed += (float) (StrictMath.pow(efficiencyModifier, 2) + 1);
            }
        }

        if (mc.player.hasStatusEffect(StatusEffects.HASTE))
            digSpeed *= 1 + (Objects.requireNonNull(mc.player.getStatusEffect(StatusEffects.HASTE)).getAmplifier() + 1) * 0.2F;


        if (mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE))
            digSpeed *= (float) Math.pow(0.3f, Objects.requireNonNull(mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier() + 1);


        if (mc.player.isSubmergedInWater())
            digSpeed *= (float) mc.player.getAttributeInstance(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED).getValue();

        return digSpeed < 0 ? 0 : digSpeed;
    }

    private static float getDestroySpeed(BlockState state, ItemStack itemStack) {
        float destroySpeed = 1;

        if (mc.player == null)
            return 0;
        if (itemStack != null && !itemStack.isEmpty()) {
            destroySpeed *= itemStack.getMiningSpeedMultiplier(state);
        }

        return destroySpeed;
    }

    public static boolean isTool(Item item) {
        return item instanceof ToolItem || item instanceof ShearsItem;
    }

    public static boolean isTool(ItemStack itemStack) {
        return isTool(itemStack.getItem());
    }

    public static boolean isFood(ItemStack itemStack) {
        return itemStack.contains(DataComponentTypes.FOOD);
    }
}
