package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.api.SoundSystem.Sounds;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEvents.class)
public class MixinSoundEvents {

    @Inject(method = "register(Ljava/lang/String;)Lnet/minecraft/sound/SoundEvent;", at = @At("HEAD"))
    private static void modifyRegister(String id, CallbackInfoReturnable<SoundEvent> cir) {
        Sounds.initSounds();
    }
}
