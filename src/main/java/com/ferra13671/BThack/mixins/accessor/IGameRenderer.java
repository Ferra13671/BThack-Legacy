package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface IGameRenderer {

    @Accessor("blurPostProcessor")
    PostEffectProcessor getBlurPostProcessor();
}
