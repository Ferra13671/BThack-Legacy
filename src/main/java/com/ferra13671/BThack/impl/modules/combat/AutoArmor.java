package com.ferra13671.BThack.impl.modules.combat;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.InventoryUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "AutoArmor", description = "lang.module.AutoArmor", category = "COMBAT")
public class AutoArmor extends Module {

    public final ModeSetting swap = new ModeSetting("Swap", this, Arrays.asList("Client", "Packet"));
    public final ModeSetting filter = new ModeSetting("Filter", this, Arrays.asList("Best", "Worst"));
    public final BooleanSetting enchFilter = new BooleanSetting("Enchantment Filter", this, true);
    public final BooleanSetting allowInventory = new BooleanSetting("Allow Inventory", this, true);
    public final BooleanSetting allowReplace = new BooleanSetting("Allow Replace", this, true);


    public List<SlotInfo> slotInfos = new ArrayList<>();
    public List<SlotInfo> bestSlots = new ArrayList<>();

    @Override
    public void onEnable() {
        slotInfos.clear();
        super.onEnable();
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        bestSlots = new ArrayList<>();

        findAction();
        filterAction();
        swapAction();
    }

    @SuppressWarnings("DataFlowIssue")
    public void findAction() {
        slotInfos.clear();
        for (int i = 0; i < (allowInventory.getValue() ? 36 : 9); i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof ArmorItem)
                slotInfos.add(new SlotInfo(stack, i, false));
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void filterAction() {
        int bestHelmetSlot = -1;
        int bestHelmetScore = getScore(mc.player.getInventory().getArmorStack(3));
        boolean bestHelmetChest = false;

        int bestChestplateSlot = -1;
        int bestChestplateScore = getScore(mc.player.getInventory().getArmorStack(2));
        boolean bestChestplateChest = false;

        int bestLegsSlot = -1;
        int bestLegsScore = getScore(mc.player.getInventory().getArmorStack(1));
        boolean bestLegsChest = false;

        int bestBootsSlot = -1;
        int bestBootsScore = getScore(mc.player.getInventory().getArmorStack(0));
        boolean bestBootsChest = false;

        for (SlotInfo info : slotInfos) {
            if (info.stack.getItem() instanceof ArmorItem) {
                int score = getScore(info.stack);
                int slotId = info.stack.get(DataComponentTypes.EQUIPPABLE).slot().getEntitySlotId();
                if (slotId == EquipmentSlot.HEAD.getEntitySlotId()) {
                    if (filter(bestHelmetScore, score)) {
                        bestHelmetScore = score;
                        bestHelmetSlot = info.slot;
                        bestHelmetChest = info.chest;
                    }
                } else
                if (slotId == EquipmentSlot.CHEST.getEntitySlotId()) {
                    if (filter(bestChestplateScore, score)) {
                        bestChestplateScore = score;
                        bestChestplateSlot = info.slot;
                        bestChestplateChest = info.chest;
                    }
                } else
                if (slotId == EquipmentSlot.LEGS.getEntitySlotId()) {
                    if (filter(bestLegsScore, score)) {
                        bestLegsScore = score;
                        bestLegsSlot = info.slot;
                        bestLegsChest = info.chest;
                    }
                } else {
                    if (filter(bestBootsScore, score)) {
                        bestBootsScore = score;
                        bestBootsSlot = info.slot;
                        bestBootsChest = info.chest;
                    }
                }
            }
        }
        bestSlots = Arrays.asList(null, null, null, null);
        if (bestHelmetSlot != -1)
            bestSlots.set(3, new SlotInfo(null, bestHelmetSlot, bestHelmetChest));
        if (bestChestplateSlot != -1)
            bestSlots.set(2, new SlotInfo(null, bestChestplateSlot, bestChestplateChest));
        if (bestLegsSlot != -1)
            bestSlots.set(1, new SlotInfo(null, bestLegsSlot, bestLegsChest));
        if (bestBootsSlot != -1)
            bestSlots.set(0, new SlotInfo(null, bestBootsSlot, bestBootsChest));
    }

    @SuppressWarnings("DataFlowIssue")
    public void swapAction() {
        SlotInfo helmetSlot = bestSlots.get(3);
        SlotInfo chestplateSlot = bestSlots.get(2);
        SlotInfo legsSlot = bestSlots.get(1);
        SlotInfo bootsSlot = bestSlots.get(0);
        if (helmetSlot != null && isEmptyArmor(3))
            InventoryUtils.replaceItems(InventoryUtils.HEAD_SLOT, (helmetSlot.slot < 9 ? helmetSlot.slot + 36 : helmetSlot.slot));
        if (chestplateSlot != null && mc.player.getInventory().getArmorStack(2).getItem() != Items.ELYTRA && isEmptyArmor(2))
            InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, (chestplateSlot.slot < 9 ? chestplateSlot.slot + 36 : chestplateSlot.slot));
        if (legsSlot != null && isEmptyArmor(1))
            InventoryUtils.replaceItems(InventoryUtils.LEGS_SLOT, (legsSlot.slot < 9 ? legsSlot.slot + 36 : legsSlot.slot));
        if (bootsSlot != null && isEmptyArmor(0))
            InventoryUtils.replaceItems(InventoryUtils.FEET_SLOT, (bootsSlot.slot < 9 ? bootsSlot.slot + 36 : bootsSlot.slot));
    }

    public int getScore(ItemStack stack) {
        Item item = stack.getItem();
        int score = 0;
        if (item instanceof ArmorItem) {
            score += stack.getMaxDamage();
            /*
            ArmorMaterial material = armor.getMaterial().value();
            if (material == ArmorMaterials.LEATHER.value())
                score++;
            if (material == ArmorMaterials.CHAIN.value())
                score += 2;
            if (material == ArmorMaterials.GOLD.value())
                score += 3;
            if (material == ArmorMaterials.IRON.value())
                score += 4;
            if (material == ArmorMaterials.DIAMOND.value())
                score += 5;
            if (material == ArmorMaterials.NETHERITE.value())
                score += 6;

             */

            if (enchFilter.getValue())
                score += getEnchantmentScore(stack);
        }

        return score;
    }

    public int getEnchantmentScore(ItemStack stack) {
        int score = 0;
        for (RegistryEntry<Enchantment> ench : stack.getEnchantments().getEnchantments())
            if (isGoodEnchantment(ench))
                score += stack.getEnchantments().getLevel(ench);
        return score;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public boolean isGoodEnchantment(RegistryEntry<Enchantment> ench) {
        return ench.getKey().get().equals(Enchantments.BLAST_PROTECTION) ||
                ench.getKey().get().equals(Enchantments.PROTECTION) ||
                ench.getKey().get().equals(Enchantments.THORNS) ||
                ench.getKey().get().equals(Enchantments.UNBREAKING) ||
                ench.getKey().get().equals(Enchantments.MENDING);
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean isEmptyArmor(int slot) {
        return allowReplace.getValue() || mc.player.getInventory().getArmorStack(slot).isEmpty();
    }

    public boolean filter(int bestScore, int score) {
        return filter.getValue().equals("Best") ? score > bestScore : score < bestScore;
    }

    public record SlotInfo(ItemStack stack, int slot, boolean chest) {}
}
