package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import com.ferra13671.BThack.mixins.accessor.IPlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;

public class EXPCountComponent extends AbstractOneTextComponent {

    public EXPCountComponent() {
        super("EXPCount",
                5,
                235,
                false
        );
    }

    @Override
    public String getText() {
        int bootles = 0;

        for (DefaultedList<ItemStack> list : ((IPlayerInventory) mc.player.getInventory()).getCombinedInventory()) {
            for (ItemStack stack : list) {
                if (stack.getItem() == Items.EXPERIENCE_BOTTLE) {
                    bootles += stack.getCount();
                }
            }
        }

        return "EXP Bottles: " + Formatting.WHITE + bootles;
    }
}
