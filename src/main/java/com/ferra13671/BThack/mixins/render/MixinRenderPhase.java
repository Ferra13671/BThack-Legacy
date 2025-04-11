package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.impl.Modules.RENDER.EnchantColor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.util.Util;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPhase.class)
public class MixinRenderPhase implements Mc {

    @Mutable
    @Shadow @Final public static RenderPhase.Texturing GLINT_TEXTURING;

    @Mutable
    @Shadow @Final public static RenderPhase.Texturing ENTITY_GLINT_TEXTURING;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyInitTexturing(CallbackInfo ci) {
        GLINT_TEXTURING = new RenderPhase.Texturing("glint_texturing", () -> {
            newSetupGlintTexturing(8.0f);
            if (ModuleList.enchantColor.isEnabled()) {
                float[] clr = EnchantColor.getEnchantColor();
                RenderSystem.setShaderColor(clr[0], clr[1], clr[2], 1);
                RenderSystem.setShaderGlintAlpha(clr[3]);
            } else {
                RenderSystem.setShaderColor(165 / 255f, 0, 1, 1);
            }
        }, () -> {
            RenderSystem.setShaderColor(1,1,1,1);
            RenderSystem.resetTextureMatrix();
        });
        ENTITY_GLINT_TEXTURING = new RenderPhase.Texturing("entity_glint_texturing", () -> {
            newSetupGlintTexturing(0.16f);
            if (ModuleList.enchantColor.isEnabled()) {
                float[] clr = EnchantColor.getEnchantColor();
                RenderSystem.setShaderColor(clr[0], clr[1], clr[2], 1);
                RenderSystem.setShaderGlintAlpha(clr[3]);
            }
        }, () -> {
            RenderSystem.setShaderColor(1,1,1,1);
            RenderSystem.resetTextureMatrix();
        });
    }

    @Unique
    private static void newSetupGlintTexturing(float scale) {
        scale /= ModuleList.enchantColor.enchantSize.getValue().floatValue();
        long l = (long)((double) Util.getMeasuringTimeMs() * (mc.options.getGlintSpeed().getValue() * ModuleList.enchantColor.enchantSpeed.getValue()) * 8.0);
        float f = (float)(l % 110000L) / 110000.0F;
        float g = (float)(l % 30000L) / 30000.0F;
        Matrix4f matrix4f = (new Matrix4f()).translation(-f, g, 0.0F);
        matrix4f.rotateZ(0.17453292F).scale(scale);
        RenderSystem.setTextureMatrix(matrix4f);
    }
}
