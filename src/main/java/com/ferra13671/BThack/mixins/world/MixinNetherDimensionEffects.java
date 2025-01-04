package com.ferra13671.BThack.mixins.world;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(DimensionEffects.Nether.class)
public class MixinNetherDimensionEffects {

    @Inject(method = "adjustFogColor", at = @At("HEAD"), cancellable = true)
    public void modifyGetFogColor(Vec3d par1, float par2, CallbackInfoReturnable<Vec3d> cir) {
        if (ModuleList.fogColor.isEnabled() && ModuleList.fogColor.nether.getValue()) {
            if (ModuleList.fogColor.rainbow.getValue()) {
                Color color = new Color(ColorUtils.rainbowType(2));
                cir.setReturnValue(new Vec3d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f));
            } else
                cir.setReturnValue(new Vec3d(ModuleList.fogColor.fogRed.getValue() / 255, ModuleList.fogColor.fogGreen.getValue() / 255, ModuleList.fogColor.fogBlue.getValue() / 255));
        }
    }
}
