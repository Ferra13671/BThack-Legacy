package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Shaders.Shaders;
import com.ferra13671.BThack.mixins.accessor.IWorldRenderer;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.GameMode;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow private boolean renderingPanorama;

    @Shadow protected abstract float getFov(Camera camera, float tickDelta, boolean changingFov);

    @Shadow protected abstract void tiltViewWhenHurt(MatrixStack matrices, float tickDelta);

    @Shadow @Final
    private MinecraftClient client;

    @Shadow protected abstract void bobView(MatrixStack matrices, float tickDelta);

    @Shadow @Final private LightmapTextureManager lightmapTextureManager;

    @Shadow @Final public HeldItemRenderer firstPersonRenderer;

    @Shadow public abstract Matrix4f getBasicProjectionMatrix(float fovDegrees);

    @Shadow @Final private BufferBuilderStorage buffers;

    @Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
    public void modifyShouldRenderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleList.blockHighlight.isEnabled()) cir.setReturnValue(false);
    }

    @Inject(method = "renderWorld", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
    private void modifyRenderHandOnRenderWorld(RenderTickCounter tickCounter, CallbackInfo ci, @Local(ordinal = 1) Matrix4f matrix4f2, @Local(ordinal = 1) float tickDelta) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix(matrix4f2);
        BThackRender.worldMatrixStack = matrixStack;
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        RenderWorldLastEvent event = new RenderWorldLastEvent(BThackRender.worldMatrixStack);
        BThack.EVENT_BUS.activate(event);
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", shift = At.Shift.BEFORE))
    public void modifyRenderWorldAfterRenderHand(RenderTickCounter tickCounter, CallbackInfo ci) {
        if (ModuleList.shaders.isEnabled()) ModuleList.shaders.drawShader(tickCounter.getTickDelta(true));
    }

    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    public void modifyTiltViewWhenHurtInRenderHand(GameRenderer instance, MatrixStack matrices, float tickDelta) {
        if ((ModuleList.handTweaks.isEnabled() && ModuleList.handTweaks.noBob.getValue()))
            tiltViewWhenHurt(matrices, tickDelta);

        if (ModuleList.handTweaks.isEnabled() && ModuleList.handTweaks.handAnimStep.getValue().floatValue() == 1f) {
            client.player.renderYaw = client.player.lastRenderYaw = client.player.yaw;
            client.player.renderPitch = client.player.lastRenderPitch = client.player.pitch;
        }
    }

    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    public void modifyBobViewInRenderHand(GameRenderer instance, MatrixStack matrices, float tickDelta) {
        if (!(ModuleList.handTweaks.isEnabled() && ModuleList.handTweaks.noBob.getValue()))
            bobView(matrices, tickDelta);
    }

    @Inject(method = "renderHand", at = @At("RETURN"))
    public void modifyRenderHandPost(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        if (ModuleList.shaders.isEnabled() && ModuleList.shaders.hands.getValue()) {
            renderShaderHand(camera, tickDelta);
            ((IWorldRenderer) client.worldRenderer)._getBufferBuilders().getOutlineVertexConsumers().draw();
            client.getFramebuffer().beginWrite(false);
        }
    }

    @Inject(method = "bobView", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;", shift = At.Shift.AFTER), cancellable = true)
    public void modifyBobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (ModuleList.freeCam.isEnabled()) {
            ci.cancel();
            return;
        }
        if (ModuleList.customBob.isEnabled()) {
            ci.cancel();
            if (ModuleList.customBob.getFullStrength() == 0) return;
            ModuleList.customBob.customBob(matrices);
        }
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void modifyTiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.hurtCam.getValue())
            ci.cancel();
    }

    @ModifyExpressionValue(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
    public float modifyLerpInRenderWorld(float original) {
        return (ModuleList.noRender.isEnabled() && ModuleList.noRender.nausea.getValue()) ? 0 : original;
    }

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    public void modifyShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.getItem() == Items.TOTEM_OF_UNDYING && ModuleList.noRender.isEnabled() && ModuleList.noRender.totemAnimation.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void modifyRender(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        Shaders.INSTANCE.updateTime();
    }

    @ModifyReturnValue(method = "getFov",at = @At("RETURN"))
    public float modifyGetFov(float original) {
        return ModuleList.zoom.isEnabled() && ModuleList.zoom.needZoom() ? ModuleList.zoom.getFov(original) : original;
    }

    @Unique
    public void renderShaderHand(Camera camera, float tickDelta) {
        if (!renderingPanorama) {
            Matrix4f matrix4f2 = getBasicProjectionMatrix(this.getFov(camera, tickDelta, false));
            RenderSystem.setProjectionMatrix(matrix4f2, ProjectionType.PERSPECTIVE);
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.push();
            if (!(ModuleList.handTweaks.isEnabled() && ModuleList.handTweaks.noBob.getValue())) {
                tiltViewWhenHurt(matrixStack, tickDelta);
                if (client.options.getBobView().getValue())
                    bobView(matrixStack, tickDelta);
            }

            boolean bl = this.client.getCameraEntity() instanceof LivingEntity && ((LivingEntity) client.getCameraEntity()).isSleeping();
            if (client.options.getPerspective().isFirstPerson() && !bl && !client.options.hudHidden && client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
                lightmapTextureManager.enable();
                firstPersonRenderer.renderItem(tickDelta, matrixStack, buffers.getEntityVertexConsumers(), client.player, client.getEntityRenderDispatcher().getLight(client.player, tickDelta));
                lightmapTextureManager.disable();
            }

            matrixStack.pop();
            if (client.options.getPerspective().isFirstPerson() && !bl) {
                VertexConsumerProvider.Immediate immediate = buffers.getEntityVertexConsumers();
                InGameOverlayRenderer.renderOverlays(client, matrixStack, immediate);
                immediate.draw();
            }

        }
    }
}
