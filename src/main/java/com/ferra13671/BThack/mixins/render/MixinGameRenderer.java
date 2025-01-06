package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.impl.Modules.RENDER.NoOverlay;
import com.ferra13671.BThack.impl.Modules.RENDER.NoRender;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void modifyTiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (ModuleList.noOverlay.isEnabled() && NoOverlay.hurtCam.getValue())
            ci.cancel();
    }

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.getItem() == Items.TOTEM_OF_UNDYING && ModuleList.noRender.isEnabled() && NoRender.totemAnimation.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "renderNausea", at = @At("HEAD"), cancellable = true)
    public void modifyRenderNausea(DrawContext context, float distortionStrength, CallbackInfo ci) {
        if (ModuleList.noRender.isEnabled() && NoRender.nausea.getValue())
            ci.cancel();
    }
}
