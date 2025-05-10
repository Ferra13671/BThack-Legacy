package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.class)
public interface IAbstractBlock {

    @Accessor("collidable")
    boolean getCollidable();
}
