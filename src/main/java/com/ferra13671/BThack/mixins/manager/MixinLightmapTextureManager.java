package com.ferra13671.BThack.mixins.manager;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.LightmapGammaColorEvent;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LightmapTextureManager.class)
public class MixinLightmapTextureManager {

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"))
    private void hookUpdate(Args args) {
        LightmapGammaColorEvent event = new LightmapGammaColorEvent(args.get(2));
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled())
            args.set(2, event.gammaColor);
    }
}
