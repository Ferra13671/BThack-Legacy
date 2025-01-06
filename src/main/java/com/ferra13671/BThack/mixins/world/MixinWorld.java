package com.ferra13671.BThack.mixins.world;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.impl.Modules.WORLD.CustomDayTime;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {

    @Inject(method = "getTimeOfDay", at = @At("HEAD"), cancellable = true)
    public void modifyGetTimeOfDay(CallbackInfoReturnable<Long> cir) {
        if (ModuleList.customDayTime.isEnabled())
            cir.setReturnValue(CustomDayTime.time);
    }
}
