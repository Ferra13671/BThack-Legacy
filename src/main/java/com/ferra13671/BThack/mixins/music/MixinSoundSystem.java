package com.ferra13671.BThack.mixins.music;

import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(SoundSystem.class)
public class MixinSoundSystem {
    /*
    Why can't the Mojangs do their code right????
    No bitch, we're the ones who have to take this shit and fix it.









    Kill me, please...
     */


    @Shadow private boolean started;

    @Shadow @Final private Map<SoundInstance, Integer> soundEndTicks;

    @Shadow private int ticks;

    @Shadow @Final private Map<SoundInstance, Channel.SourceManager> sources;

    @Inject(method = "isPlaying", at = @At("HEAD"), cancellable = true)
    public void modifyIsPlaying(SoundInstance sound, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        if (!started) {
            cir.setReturnValue(false);
        } else {
            Integer i = soundEndTicks.get(sound);
            if (i != null && i <= ticks)
                cir.setReturnValue(true);
            cir.setReturnValue(sources.containsKey(sound));
        }
    }
}
