package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {

    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void modifyApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customFogDistance.getValue()) {
            RenderSystem.setShaderFogStart(ModuleList.ambience.fogStart.getValue().floatValue());
            RenderSystem.setShaderFogEnd(ModuleList.ambience.fogEnd.getValue().floatValue());
        } else if (ModuleList.noFog.isEnabled()) {
            if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
                RenderSystem.setShaderFogStart(viewDistance * 4);
                RenderSystem.setShaderFogEnd(viewDistance * 4.25f);
            }
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"))
    private static float modifyGetRainGradientInRender(ClientWorld instance, float v) {
        return ModuleList.ambience.getRainGradient(instance.getRainGradient(v));
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getThunderGradient(F)F"))
    private static float modifyGetThunderGradientInRender(ClientWorld instance, float v) {
        return ModuleList.ambience.getThunderGradient(instance.getThunderGradient(v));
    }
}
