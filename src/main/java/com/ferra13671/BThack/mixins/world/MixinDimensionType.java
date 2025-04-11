package com.ferra13671.BThack.mixins.world;

import com.ferra13671.BThack.Core.Client.ModuleList;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class MixinDimensionType {

    @Inject(method = "getMoonPhase", at = @At("HEAD"), cancellable = true)
    public void modifyMoonPhase(long time, CallbackInfoReturnable<Integer> cir) {
        if (ModuleList.worldElements.isEnabled() && ModuleList.worldElements.changeMoonPhase.getValue())
            cir.setReturnValue(ModuleList.worldElements.moonPhase.getValue().intValue());
    }
}
