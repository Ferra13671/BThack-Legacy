package com.ferra13671.BThack.api.imixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public interface ModifyHeldItemRenderer {

    void renderShaderItem(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, ClientPlayerEntity player, int light);
}
