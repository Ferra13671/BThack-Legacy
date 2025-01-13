package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.Core.Client.ModuleList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class MixinInGameOverlayRenderer {

    @Inject(method = "renderOverlays", at = @At("HEAD"), cancellable = true)
    private static void modifyRenderOverlays(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (ModuleList.noRender.isEnabled() && ModuleList.noRender.overlay.getValue()) ci.cancel();
    }
}
