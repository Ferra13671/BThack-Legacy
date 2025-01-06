package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Arrays;

public class ElytraSwap extends OneActionModule {

    public static ModeSetting moveType;

    public ElytraSwap() {
        super("ElytraSwap",
                "lang.module.ElytraSwap",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        moveType = new ModeSetting("Move Type", this, Arrays.asList("Swap", "Pickup"));

        initSettings(
                moveType
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        Item armor = mc.player.getInventory().getArmorStack(2).getItem();
        if (armor instanceof ElytraItem) {
            for (int needSlot = 0; needSlot < 36; needSlot++) {
                if (mc.player.getInventory().getStack(needSlot).getItem() instanceof ArmorItem armorItem) {
                    if (armorItem.getSlotType() == EquipmentSlot.CHEST) {
                        int item;
                        if (needSlot < 9)
                            item = needSlot + 36;
                        else
                            item = needSlot;

                        if (moveType.getValue().equals("Swap")) {
                            pc.clickSlot(0, InventoryUtils.CHESTPLATE_SLOT, 0, SlotActionType.QUICK_MOVE);
                            pc.clickSlot(0, item, 0, SlotActionType.QUICK_MOVE);
                        } else {
                            InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, item);
                        }
                        toggle();
                        break;
                    }
                }
            }
        } else {
            for (int needSlot = 0; needSlot < 36; needSlot++) {
                if (mc.player.getInventory().getStack(needSlot).getItem() instanceof ElytraItem) {
                    int item;
                    if (needSlot < 9)
                        item = needSlot + 36;
                    else
                        item = needSlot;

                    InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, item);
                    toggle();
                    break;
                }
            }
        }
    }
}
