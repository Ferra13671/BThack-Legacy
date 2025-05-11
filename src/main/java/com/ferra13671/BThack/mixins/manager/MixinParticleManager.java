package com.ferra13671.BThack.mixins.manager;

import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.core.Client.ModuleList;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class MixinParticleManager {


    @Inject(method = "renderParticles", at = @At("HEAD"), cancellable = true)
    public void modifyRenderParticles(LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
        if (Client.isOptionActivated(ModuleList.noRender, ModuleList.noRender.particles))
            ci.cancel();
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void modifyParticleManagerTick(CallbackInfo ci) {
        if (Client.isOptionActivated(ModuleList.noRender, ModuleList.noRender.particles))
            ci.cancel();
    }
}
