package com.ferra13671.BThack.mixins.world;

import com.ferra13671.BThack.core.Client.ModuleList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeParticleConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BiomeEffects.class)
public class MixinBiomeEffects {

    @Inject(method = "getParticleConfig", at = @At("HEAD"), cancellable = true)
    public void modifyGetParticleConfig(CallbackInfoReturnable<Optional<BiomeParticleConfig>> cir) {
        if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customWeather.getValue() && ModuleList.ambience.isParticleWeather())
            cir.setReturnValue(Optional.of(new BiomeParticleConfig(ParticleTypes.WHITE_ASH, 0.118093334f * ModuleList.ambience.ashProbability.getValue().floatValue())));
    }
}
