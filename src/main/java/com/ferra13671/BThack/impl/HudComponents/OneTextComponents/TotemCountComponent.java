package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import com.ferra13671.BThack.mixins.accessor.IPlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;

public class TotemCountComponent extends AbstractOneTextComponent {

    public TotemCountComponent() {
        super("TotemCount",
                5,
                255,
                false
        );
    }

    @Override
    public String getText() {
        int totems = 0;

        for (DefaultedList<ItemStack> list : ((IPlayerInventory) mc.player.getInventory()).getCombinedInventory()) {
            for (ItemStack stack : list) {
                if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                    totems += stack.getCount();
                }
            }
        }

        return "Totems: " + Formatting.WHITE + totems;
    }
}
