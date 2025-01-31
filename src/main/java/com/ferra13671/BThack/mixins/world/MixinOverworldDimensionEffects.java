package com.ferra13671.BThack.mixins.world;

import com.ferra13671.BThack.Core.Client.ModuleList;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionEffects.Overworld.class)
public class MixinOverworldDimensionEffects {

    @Inject(method = "adjustFogColor", at = @At("HEAD"), cancellable = true)
    public void modifyGetFogColor(Vec3d par1, float par2, CallbackInfoReturnable<Vec3d> cir) {
        if (ModuleList.fogColor.isEnabled() && ModuleList.fogColor.overworld.getValue()) {
            cir.setReturnValue(ModuleList.fogColor.getFogColor());
        }
    }
}
