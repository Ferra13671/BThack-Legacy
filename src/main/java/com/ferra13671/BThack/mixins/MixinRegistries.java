package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.api.SoundSystem.Sounds;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Registries.class)
public class MixinRegistries {

    @Inject(method = "bootstrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registries;freezeRegistries()V", shift = At.Shift.BEFORE))
    private static void modifyBootstrapRegistriesBeforeFreeze(CallbackInfo ci) {
        Sounds.initSounds();
    }
}
