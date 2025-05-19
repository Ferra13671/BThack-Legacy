package com.ferra13671.BThack.mixins.manager;

import com.ferra13671.BThack.core.Client.ModuleList;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LightmapTextureManager.class)
public class MixinLightmapTextureManager {

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Uniform;set(F)V", ordinal = 5))
    public void modifySetDarkenWorldFactor(Args args) {
        if (ModuleList.fullBright.isEnabled() && ModuleList.fullBright.mode.getValue().equals("Gamma")) {
            args.set(0, -100f);
        }
    }
}
