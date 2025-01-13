package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DrawContext.class)
public interface IDrawContext {

    @Accessor("vertexConsumers")
    VertexConsumerProvider.Immediate getVertexConsumers();
}
