package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.Events.Render.RenderWorldEvent;
import com.ferra13671.MegaEvents.Base.Event;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Final
    @Shadow
    private BufferBuilderStorage bufferBuilders;
    @Shadow private ClientWorld world;
    @Shadow private PostEffectProcessor transparencyPostProcessor;

    @Inject(method = "render", at = @At("HEAD"))
    private void beforeRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        BThackRenderUtils.lastWorldMatrix.set(matrices.peek().getPositionMatrix());
        BThackRenderUtils.lastProjMatrix.set(RenderSystem.getProjectionMatrix());
        BThackRenderUtils.lastModViewMatrix.set(RenderSystem.getModelViewMatrix());

        BThackRender.worldRenderContext.prepare((WorldRenderer) (Object) this, matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, lightmapTextureManager, matrix4f, bufferBuilders.getEntityVertexConsumers(), world.getProfiler(), transparencyPostProcessor != null, world);
    }

    @Inject(method = "setupTerrain", at = @At("RETURN"))
    private void afterTerrainSetup(Camera camera, Frustum frustum, boolean hasForcedFrustum, boolean spectator, CallbackInfo ci) {
        BThackRender.worldRenderContext.setFrustum(frustum);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDDLorg/joml/Matrix4f;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void afterTerrainSolid(CallbackInfo ci) {
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        BThack.EVENT_BUS.activate(new RenderWorldEvent.BeforeEntities(BThackRender.worldRenderContext));
    }

    @Inject(method = "render", at = @At(value = "CONSTANT", args = "stringValue=blockentities", ordinal = 0))
    private void afterEntities(CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new RenderWorldEvent.AfterEntities(BThackRender.worldRenderContext));
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    private void onDrawBlockOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        BThackRender.worldRenderContext.prepareBlockOutline(entity, cameraX, cameraY, cameraZ, blockPos, blockState);

        Event event = new RenderWorldEvent.BlockOutline(BThackRender.worldRenderContext, BThackRender.worldRenderContext);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.cancel();
        }

        // The immediate mode VertexConsumers use a shared buffer, so we have to make sure that the immediate mode VCP
        // can accept block outline lines rendered to the existing vertexConsumer by the vanilla block overlay.
        BThackRender.worldRenderContext.consumers().getBuffer(RenderLayer.getLines());
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderChunkDebugInfo(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/Camera;)V"))
    private void onChunkDebugRender(CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new RenderWorldEvent.Last(BThackRender.worldRenderContext));
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void afterRender(CallbackInfo ci) {
        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
        BThack.EVENT_BUS.activate(new RenderWorldEvent.End(BThackRender.worldRenderContext));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    public void modifyRenderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (ModuleList.noWeather.isEnabled())
            ci.cancel();
    }

    @Inject(method = "tickRainSplashing", at = @At("HEAD"), cancellable = true)
    public void modifyTickRainSplashing(Camera camera, CallbackInfo ci) {
        if (ModuleList.noWeather.isEnabled())
            ci.cancel();
    }
}
