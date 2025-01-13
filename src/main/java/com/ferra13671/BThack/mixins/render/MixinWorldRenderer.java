package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Inject(method = "render", at = @At("HEAD"))
    private void beforeRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        BThackRenderUtils.lastWorldMatrix.set(positionMatrix);
        BThackRenderUtils.lastProjMatrix.set(RenderSystem.getProjectionMatrix());
        BThackRenderUtils.lastModViewMatrix.set(RenderSystem.getModelViewMatrix());
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
