package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface IGameRenderer {

    @Accessor("blurPostProcessor")
    PostEffectProcessor getBlurPostProcessor();

    @Invoker("findCrosshairTarget")
    HitResult _findCrosshairTarget(Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta);
}
