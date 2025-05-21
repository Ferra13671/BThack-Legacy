package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.api.SoundSystem.Sounds;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEvents.class)
public class MixinSoundEvents {

    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void modifyStaticInit(CallbackInfo ci) {
        Sounds.initSounds();
    }
}
