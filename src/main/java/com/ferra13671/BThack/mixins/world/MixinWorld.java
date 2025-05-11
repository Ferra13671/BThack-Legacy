package com.ferra13671.BThack.mixins.world;

import com.ferra13671.BThack.Core.Client.ModuleList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {

    @Inject(method = "getTimeOfDay", at = @At("HEAD"), cancellable = true)
    public void modifyGetTimeOfDay(CallbackInfoReturnable<Long> cir) {
        if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customWorldTime.getValue())
            cir.setReturnValue(ModuleList.ambience.getWorldTime());
    }

    @Redirect(method = "calculateAmbientDarkness", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getRainGradient(F)F"))
    public float modifyGetRainGradientInCalculateAmbientDarkness(World instance, float delta) {
        return ModuleList.ambience.getRainGradient(instance.getRainGradient(delta));
    }

    @Redirect(method = "calculateAmbientDarkness", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getThunderGradient(F)F"))
    public float getThunderGradientInCalculateAmbientDarkness(World instance, float delta) {
        return ModuleList.ambience.getThunderGradient(instance.getThunderGradient(delta));
    }
}
