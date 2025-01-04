package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PlayerInventory.class)
public interface IPlayerInventory {

    @Accessor("combinedInventory")
    List<DefaultedList<ItemStack>> getCombinedInventory();
}
