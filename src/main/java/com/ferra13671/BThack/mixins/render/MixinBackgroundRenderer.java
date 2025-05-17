package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.core.Client.ModuleList;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {

    @Inject(method = "applyFog", at = @At(value = "RETURN"))
    private static void modifyApplyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {
        Fog fog = cir.getReturnValue();
        if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customFogDistance.getValue()) {
            fog = new Fog(ModuleList.ambience.fogStart.getValue().floatValue(), ModuleList.ambience.fogEnd.getValue().floatValue(), fog.shape(), fog.red(), fog.green(), fog.blue(), fog.alpha());
            cir.setReturnValue(fog);
        } else if (ModuleList.noFog.isEnabled()) {
            if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
                fog = new Fog(viewDistance * 4, viewDistance * 4.25f, fog.shape(), fog.red(), fog.green(), fog.blue(), fog.alpha());
                cir.setReturnValue(fog);
            }
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
