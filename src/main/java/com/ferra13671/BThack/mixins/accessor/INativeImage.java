package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NativeImage.class)
public interface INativeImage {

    @Accessor("pointer")
    long getPointer();
}
