package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.core.Client.ModuleList;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticlesMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherRendering.class)
public class MixinWeatherRendering {

    @Redirect(method = "renderPrecipitation(Lnet/minecraft/world/World;Lnet/minecraft/client/render/VertexConsumerProvider;IFLnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getRainGradient(F)F"))
    public float modifyGetRainGradientInRenderWeather(World instance, float delta) {
        return ModuleList.ambience.getRainGradient(instance.getRainGradient(delta));
    }

    @Inject(method = "addParticlesAndSound", at = @At("HEAD"), cancellable = true)
    public void modifyTickRainSplashing(ClientWorld world, Camera camera, int ticks, ParticlesMode particlesMode, CallbackInfo ci) {
        if (ModuleList.noWeather.isEnabled() && (!ModuleList.ambience.isEnabled() || !ModuleList.ambience.customWeather.getValue()))
            ci.cancel();
    }

    @Redirect(method = "addParticlesAndSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"))
    public float modifyGetRainGradientInTicRainSplashing(ClientWorld instance, float v) {
        return ModuleList.ambience.getRainGradient(instance.getRainGradient(v));
    }
}
