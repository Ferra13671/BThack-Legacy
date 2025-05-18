package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.core.Client.ModuleList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticlesMode;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(WeatherRendering.class)
public abstract class MixinWeatherRendering {

    @Shadow protected abstract void buildPrecipitationPieces(World world, int ticks, float delta, Vec3d pos, int range, List<WeatherRendering.Piece> rainOut, List<WeatherRendering.Piece> snowOut);

    @Shadow protected abstract void renderPrecipitation(VertexConsumerProvider vertexConsumers, Vec3d pos, int range, float gradient, List<WeatherRendering.Piece> rainPieces, List<WeatherRendering.Piece> snowPieces);

    //ViaFabricPlus does not allow @Redirect to be used in this method
    @Inject(method = "renderPrecipitation(Lnet/minecraft/world/World;Lnet/minecraft/client/render/VertexConsumerProvider;IFLnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
    public void modifyRenderPrecipitation(World world, VertexConsumerProvider vertexConsumers, int ticks, float delta, Vec3d pos, CallbackInfo ci) {
        if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customWeather.getValue() && (!ModuleList.ambience.weather.getValue().equals("Clear") && !ModuleList.ambience.isParticleWeather())) {
            ci.cancel();
            float f = ModuleList.ambience.getRainGradient(world.getRainGradient(delta));
            if (!(f <= 0.0F)) {
                int i = MinecraftClient.isFancyGraphicsOrBetter() ? 10 : 5;
                List<WeatherRendering.Piece> list = new ArrayList<>();
                List<WeatherRendering.Piece> list2 = new ArrayList<>();
                buildPrecipitationPieces(world, ticks, delta, pos, i, list, list2);
                if (!list.isEmpty() || !list2.isEmpty()) {
                    renderPrecipitation(vertexConsumers, pos, i, f, list, list2);
                }

            }
        }
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
