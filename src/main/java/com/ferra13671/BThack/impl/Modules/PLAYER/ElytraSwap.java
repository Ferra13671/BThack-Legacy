package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Arrays;

@ModuleInfo(name = "ElytraSwap", description = "lang.module.ElytraSwap", category = "PLAYER")
public class ElytraSwap extends OneActionModule {

    public final ModeSetting moveType = new ModeSetting("Move Type", this, Arrays.asList("Swap", "Pickup"));


    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        Item armor = mc.player.getInventory().getArmorStack(2).getItem();
        if (armor instanceof ElytraItem) {
            if (!equipChestplate()) removeChestplateOrElytra();
        } else if (armor instanceof ArmorItem){
            if (!equipElytra()) removeChestplateOrElytra();
        } else if (!equipChestplate()) equipElytra();
    }

    private boolean equipChestplate() {
        for (int needSlot = 0; needSlot < 36; needSlot++) {
            if (mc.player.getInventory().getStack(needSlot).getItem() instanceof ArmorItem armorItem) {
                if (armorItem.getSlotType() == EquipmentSlot.CHEST) {
                    int item = needSlot < 9 ? needSlot + 36 : needSlot;

                    if (moveType.getValue().equals("Swap")) {
                        mc.interactionManager.clickSlot(0, InventoryUtils.CHESTPLATE_SLOT, 0, SlotActionType.QUICK_MOVE, mc.player);
                        mc.interactionManager.clickSlot(0, item, 0, SlotActionType.QUICK_MOVE, mc.player);
                    } else
                        InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, item);
                    toggle();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean equipElytra() {
        for (int needSlot = 0; needSlot < 36; needSlot++) {
            if (mc.player.getInventory().getStack(needSlot).getItem() instanceof ElytraItem) {
                int item;
                if (needSlot < 9)
                    item = needSlot + 36;
                else
                    item = needSlot;

                InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, item);
                toggle();
                return true;
            }
        }
        return false;
    }

    private void removeChestplateOrElytra() {
        int slot = InventoryUtils.findFreeSlot();
        if (slot == -1) return;
        int item = slot < 9 ? slot + 36 : slot;

        if (moveType.getValue().equals("Swap")) {
            mc.interactionManager.clickSlot(0, InventoryUtils.CHESTPLATE_SLOT, 0, SlotActionType.QUICK_MOVE, mc.player);
            mc.interactionManager.clickSlot(0, item, 0, SlotActionType.QUICK_MOVE, mc.player);
        } else
            InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, item);
        toggle();
    }
}
