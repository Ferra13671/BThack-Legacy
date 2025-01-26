package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "renderWorld", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
    private void modifyRenderHandOnRenderWorld(RenderTickCounter tickCounter, CallbackInfo ci, @Local(ordinal = 1) Matrix4f matrix4f2, @Local(ordinal = 1) float tickDelta) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix(matrix4f2);
        BThackRender.worldMatrixStack = matrixStack;
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        RenderWorldLastEvent event = new RenderWorldLastEvent(BThackRender.worldMatrixStack);
        BThack.EVENT_BUS.activate(event);
    }

    @Inject(method = "bobView", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;", shift = At.Shift.AFTER), cancellable = true)
    public void modifyBobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (ModuleList.customBob.isEnabled()) {
            ci.cancel();
            if (ModuleList.customBob.strength.getValue() == 0) return;
            ModuleList.customBob.customBob(matrices);
        }
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void modifyTiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.hurtCam.getValue())
            ci.cancel();
    }

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.getItem() == Items.TOTEM_OF_UNDYING && ModuleList.noRender.isEnabled() && ModuleList.noRender.totemAnimation.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "renderNausea", at = @At("HEAD"), cancellable = true)
    public void modifyRenderNausea(DrawContext context, float distortionStrength, CallbackInfo ci) {
        if (ModuleList.noRender.isEnabled() && ModuleList.noRender.nausea.getValue())
            ci.cancel();
    }
}
