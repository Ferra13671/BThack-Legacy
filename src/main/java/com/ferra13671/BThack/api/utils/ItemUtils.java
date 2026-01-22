package com.ferra13671.BThack.api.utils;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.place.PlaceManager;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.block.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public final class ItemUtils implements Mc {

    @SuppressWarnings("DataFlowIssue")
    public static void useItem(Hand hand, boolean swing, float yaw, float pitch) {
        if (swing)
            mc.player.swingHand(hand);
        Managers.NETWORK_MANAGER.sendPacket(new PlayerInteractItemC2SPacket(hand, 0, yaw, pitch));
    }

    @SuppressWarnings("DataFlowIssue")
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

    public static String getItemName(Item item) {
        return item.getTranslationKey().replace("item.minecraft.", "");
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
            return Registries.ITEM.getOptionalValue(Identifier.of(nameOrId))
                    .orElse(null);
        } catch(InvalidIdentifierException e) {
            return null;
        }
    }

    public static void useItemOnBlock(BlockPos pos) {
        BlockHitResult bhr = PlaceManager.getHitResult(pos);
        useItemOnBlock(bhr);
    }

    @SuppressWarnings("DataFlowIssue")
    public static void useItemOnBlock(BlockHitResult bhr) {
        if (bhr != null) {
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
        }
    }

    @SuppressWarnings("DataFlowIssue")
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

    public static boolean equalsEnchantment(ItemStack stack, RegistryKey<Enchantment> enchantment) {
        if (stack.isEmpty()) return false;
        Object2IntMap<RegistryEntry<Enchantment>> itemEnchantments = getEnchantments(stack);
        return equalsEnchantmentInternal(itemEnchantments, enchantment);
    }

    private static boolean equalsEnchantmentInternal(Object2IntMap<RegistryEntry<Enchantment>> itemEnchantments, RegistryKey<Enchantment> enchantmentKey) {
        for (RegistryEntry<Enchantment> enchantment : itemEnchantments.keySet()) {
            if (enchantment.matchesKey(enchantmentKey)) return true;
        }
        return false;
    }

    public static Object2IntMap<RegistryEntry<Enchantment>> getEnchantments(ItemStack itemStack) {
        Object2IntMap<RegistryEntry<Enchantment>> enchantments = new Object2IntArrayMap<>();

        if (!itemStack.isEmpty()) {
            Set<Object2IntMap.Entry<RegistryEntry<Enchantment>>> itemEnchantments = itemStack.getItem() == Items.ENCHANTED_BOOK
                    ? itemStack.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT).getEnchantmentEntries()
                    : itemStack.getEnchantments().getEnchantmentEntries();

            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantments) {
                enchantments.put(entry.getKey(), entry.getIntValue());
            }
        }
        return enchantments;
    }

    public static int getEnchantmentLevel(ItemStack itemStack, RegistryKey<Enchantment> enchantment) {
        if (itemStack.isEmpty()) return 0;
        Object2IntMap<RegistryEntry<Enchantment>> itemEnchantments = getEnchantments(itemStack);
        return getEnchantmentLevelInternal(itemEnchantments, enchantment);
    }

    private static int getEnchantmentLevelInternal(Object2IntMap<RegistryEntry<Enchantment>> itemEnchantments, RegistryKey<Enchantment> enchantment) {
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : Object2IntMaps.fastIterable(itemEnchantments)) {
            if (entry.getKey().matchesKey(enchantment)) return entry.getIntValue();
        }
        return 0;
    }

    @SuppressWarnings("DataFlowIssue")
    public static double getScore(ItemStack itemStack, BlockState state, Predicate<ItemStack> good) {
        if (!good.test(itemStack) || !isTool(itemStack)) return -1;
        if (!itemStack.isSuitableFor(state) && !(itemStack.getItem() instanceof SwordItem && (state.getBlock() instanceof BambooBlock || state.getBlock() instanceof BambooShootBlock)) && !(itemStack.getItem() instanceof ShearsItem && state.getBlock() instanceof LeavesBlock || state.isIn(BlockTags.WOOL))) return -1;

        double score = 0;

        score += itemStack.getMiningSpeedMultiplier(state) * 1000;
        score += getEnchantmentLevel(itemStack, Enchantments.UNBREAKING);
        score += getEnchantmentLevel(itemStack, Enchantments.EFFICIENCY);
        score += getEnchantmentLevel(itemStack, Enchantments.MENDING);

        if (itemStack.getItem() instanceof SwordItem && (state.getBlock() instanceof BambooBlock || state.getBlock() instanceof BambooShootBlock))
            score += 9000 + (itemStack.get(DataComponentTypes.TOOL).getSpeed(state) * 1000);


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

    @SuppressWarnings("DataFlowIssue")
    private static float getMineSpeedInternal(BlockState state, ItemStack itemStack) {
        if (mc.player == null) return 0;
        float digSpeed = getDestroySpeed(state, itemStack);

        if (digSpeed > 1) {
            int efficiencyModifier = getEnchantmentLevel(itemStack, Enchantments.EFFICIENCY);
            if (efficiencyModifier > 0 && !itemStack.isEmpty())
                digSpeed += (float) (StrictMath.pow(efficiencyModifier, 2) + 1);
        }

        if (mc.player.hasStatusEffect(StatusEffects.HASTE))
            digSpeed *= 1 + (Objects.requireNonNull(mc.player.getStatusEffect(StatusEffects.HASTE)).getAmplifier() + 1) * 0.2F;


        if (mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE))
            digSpeed *= (float) Math.pow(0.3f, Objects.requireNonNull(mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier() + 1);


        if (mc.player.isSubmergedInWater())
            digSpeed *= (float) mc.player.getAttributeInstance(EntityAttributes.SUBMERGED_MINING_SPEED).getValue();

        return digSpeed < 0 ? 0 : digSpeed;
    }

    private static float getDestroySpeed(BlockState state, ItemStack itemStack) {
        float destroySpeed = 1;

        if (mc.player == null)
            return 0;
        if (itemStack != null && !itemStack.isEmpty())
            destroySpeed *= itemStack.getMiningSpeedMultiplier(state);

        return destroySpeed;
    }

    public static boolean isTool(Item item) {
        return isTool(item.getDefaultStack());
    }

    public static boolean isTool(ItemStack itemStack) {
        return itemStack.isIn(ItemTags.AXES) || itemStack.isIn(ItemTags.HOES) || itemStack.isIn(ItemTags.PICKAXES) || itemStack.isIn(ItemTags.SHOVELS) || itemStack.getItem() instanceof ShearsItem;
    }

    public static boolean isFood(ItemStack itemStack) {
        return itemStack.contains(DataComponentTypes.FOOD);
    }
}
