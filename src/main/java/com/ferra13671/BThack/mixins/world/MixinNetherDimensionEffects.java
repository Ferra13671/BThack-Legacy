package com.ferra13671.BThack.mixins.world;

import com.ferra13671.BThack.core.Client.ModuleList;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionEffects.Nether.class)
public class MixinNetherDimensionEffects {

    @Inject(method = "adjustFogColor", at = @At("HEAD"), cancellable = true)
    public void modifyGetFogColor(Vec3d par1, float par2, CallbackInfoReturnable<Vec3d> cir) {
        if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customFogColor.getValue() && ModuleList.ambience.nether.getValue())
            cir.setReturnValue(ModuleList.ambience.getFogColor());
    }
}
