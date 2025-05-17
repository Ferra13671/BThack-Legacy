package com.ferra13671.BThack.mixins.world;

import com.ferra13671.BThack.core.Client.ModuleList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class MixinBiome {

    @Inject(method = "getPrecipitation", at = @At("HEAD"), cancellable = true)
    public void modifyGetPrecipitation(BlockPos pos, int seaLevel, CallbackInfoReturnable<Biome.Precipitation> cir) {
        if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customWeather.getValue()) {
            if (ModuleList.ambience.weather.getValue().equals("Rain") || ModuleList.ambience.weather.getValue().equals("Thunder"))
                cir.setReturnValue(Biome.Precipitation.RAIN);
            else if (ModuleList.ambience.weather.getValue().equals("Snow"))
                cir.setReturnValue(Biome.Precipitation.SNOW);
        }
    }

    @Inject(method = "hasPrecipitation", at = @At("HEAD"), cancellable = true)
    public void modifyHasPrecipitation(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customWeather.getValue() && !ModuleList.ambience.weather.getValue().equals("Clear") && !ModuleList.ambience.isParticleWeather()) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
