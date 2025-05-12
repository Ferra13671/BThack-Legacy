package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import com.ferra13671.BThack.mixins.accessor.IPlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;

@ModuleInfo(name = "CrystalCount", category = "HUD")
public class CrystalCountComponent extends AbstractOneTextComponent {

    public CrystalCountComponent() {
        super(5, 235);
    }

    @Override
    public String getText() {
        int crystals = 0;

        for (DefaultedList<ItemStack> list : ((IPlayerInventory) mc.player.getInventory()).getCombinedInventory()) {
            for (ItemStack stack : list) {
                if (stack.getItem() == Items.END_CRYSTAL) {
                    crystals += stack.getCount();
                }
            }
        }

        return "Crystals: " + Formatting.WHITE + crystals;
    }
}
