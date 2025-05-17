package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Render.TransformFirstPersonEvent;
import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {

    public MixinPlayerEntityRenderer(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "renderRightArm", at = @At("HEAD"), cancellable = true)
    public void modifyRenderRightArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Identifier skinTexture, boolean sleeveVisible, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Post(Arm.RIGHT, matrices, TransformFirstPersonEvent.TransformType.ARM);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "renderLeftArm", at = @At("HEAD"), cancellable = true)
    public void modifyRenderLeftArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Identifier skinTexture, boolean sleeveVisible, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Post(Arm.LEFT, matrices, TransformFirstPersonEvent.TransformType.ARM);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "renderArm", at = @At("HEAD"), cancellable = true)
    public void modifyAfterGetTexture(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Identifier skinTexture, ModelPart arm, boolean sleeveVisible, CallbackInfo ci) {
        if (ModuleList.shaders.isEnabled() && ModuleList.shaders.hands.getValue()) {
            ci.cancel();

            PlayerEntityModel playerEntityModel = getModel();
            arm.resetTransform();
            arm.visible = true;
            playerEntityModel.leftSleeve.visible = sleeveVisible;
            playerEntityModel.rightSleeve.visible = sleeveVisible;
            playerEntityModel.leftArm.roll = -0.1F;
            playerEntityModel.rightArm.roll = 0.1F;
            arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(skinTexture)), light, OverlayTexture.DEFAULT_UV);
        }
    }
}
