package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import com.ferra13671.BThack.mixins.accessor.IPlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;

public class GappleCountComponent extends AbstractOneTextComponent {
    public GappleCountComponent() {
        super("GappleCount",
                5,
                245,
                false
        );
    }

    @Override
    public String getText() {
        int gapples = 0;

        for (DefaultedList<ItemStack> list : ((IPlayerInventory) mc.player.getInventory()).getCombinedInventory()) {
            for (ItemStack stack : list) {
                if (stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
                    gapples += stack.getCount();
                }
            }
        }

        return "Gapples: " + Formatting.WHITE + gapples;
    }
}
