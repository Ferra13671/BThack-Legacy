package com.ferra13671.BThack.mixins.accessor;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface IEntity {

    @Invoker("setFlag")
    void invokeSetFlag(int flag, boolean set);

    @Invoker("getFlag")
    boolean invokeGetFlag(int index);

    @Accessor("fluidHeight")
    Object2DoubleMap<TagKey<Fluid>> getFluidHeight();
}
