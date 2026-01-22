package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.core.client.ModuleList;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {

    @Inject(method = "applyFog", at = @At(value = "RETURN"), cancellable = true)
    private static void modifyApplyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {
        if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
            if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customFogDistance.getValue())
                cir.setReturnValue(new Fog(ModuleList.ambience.fogStart.getValue().floatValue(), ModuleList.ambience.fogEnd.getValue().floatValue(), FogShape.CYLINDER, color.x, color.y, color.z, color.w));
            else if (ModuleList.noFog.isEnabled())
                cir.setReturnValue(new Fog(viewDistance * 4, viewDistance * 4.25f, FogShape.CYLINDER, color.x, color.y, color.z, color.w));
        }
    }

    @Redirect(method = "getFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"))
    private static float modifyGetRainGradientInRender(ClientWorld instance, float v) {
        return ModuleList.ambience.getRainGradient(instance.getRainGradient(v));
    }

    @Redirect(method = "getFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getThunderGradient(F)F"))
    private static float modifyGetThunderGradientInRender(ClientWorld instance, float v) {
        return ModuleList.ambience.getThunderGradient(instance.getThunderGradient(v));
    }
}
