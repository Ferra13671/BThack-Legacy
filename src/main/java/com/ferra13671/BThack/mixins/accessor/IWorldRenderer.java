package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface IWorldRenderer {

    @Accessor("bufferBuilders")
    BufferBuilderStorage _getBufferBuilders();

    @Accessor("entityOutlinesFramebuffer")
    Framebuffer _getEntityOutlinesFramebuffer();
}
