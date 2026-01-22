package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.events.render.RenderHandEvent;
import com.ferra13671.BThack.api.imixin.ModifyHeldItemRenderer;
import com.ferra13671.BThack.api.utils.Mc;
import com.google.common.base.MoreObjects;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer implements Mc, ModifyHeldItemRenderer {

    @Shadow
    static HeldItemRenderer.HandRenderType getHandRenderType(ClientPlayerEntity player) {
        return null;
    }

    @Shadow private float prevEquipProgressMainHand;

    @Shadow private float equipProgressMainHand;

    @Shadow private ItemStack mainHand;

    @Shadow protected abstract void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Shadow private float prevEquipProgressOffHand;

    @Shadow private float equipProgressOffHand;

    @Shadow private ItemStack offHand;

    //Modified hand renderer that uses VertexConsumerProvider instead of VertexConsumerProvider.Immediate.
    @SuppressWarnings({"AddedMixinMembersNamePattern", "DataFlowIssue"})
    @Override
    public void renderShaderItem(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, ClientPlayerEntity player, int light) {
        float f = player.getHandSwingProgress(tickDelta);
        Hand hand = MoreObjects.firstNonNull(player.preferredHand, Hand.MAIN_HAND);
        float g = player.getLerpedPitch(tickDelta);
        HeldItemRenderer.HandRenderType handRenderType = getHandRenderType(player);
        float h = MathHelper.lerp(tickDelta, player.lastRenderPitch, player.renderPitch);
        float i = MathHelper.lerp(tickDelta, player.lastRenderYaw, player.renderYaw);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((player.getPitch(tickDelta) - h) * 0.1F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((player.getYaw(tickDelta) - i) * 0.1F));
        float j;
        float k;
        if (handRenderType.renderMainHand) {
            j = hand == Hand.MAIN_HAND ? f : 0.0F;
            k = 1.0F - MathHelper.lerp(tickDelta, this.prevEquipProgressMainHand, this.equipProgressMainHand);
            this.renderFirstPersonItem(player, tickDelta, g, Hand.MAIN_HAND, j, this.mainHand, k, matrices, vertexConsumers, light);
        }

        if (handRenderType.renderOffHand) {
            j = hand == Hand.OFF_HAND ? f : 0.0F;
            k = 1.0F - MathHelper.lerp(tickDelta, this.prevEquipProgressOffHand, this.equipProgressOffHand);
            this.renderFirstPersonItem(player, tickDelta, g, Hand.OFF_HAND, j, this.offHand, k, matrices, vertexConsumers, light);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", shift = At.Shift.BEFORE))
    public void modifyRenderHeldItemInRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new RenderHandEvent(RenderHandEvent.Type.HELD_ITEM, hand, matrices));
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderArmHoldingItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IFFLnet/minecraft/util/Arm;)V"))
    public void modifyRenderArmInRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new RenderHandEvent(RenderHandEvent.Type.ARM, hand, matrices));
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("HEAD"), cancellable = true)
    public void modifyApplyEatOrDrinkTransformationPre(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, PlayerEntity player, CallbackInfo ci) {
        if (ModuleList.handTweaks.isEnabled() && ModuleList.handTweaks.noEatAnim.getValue())
            ci.cancel();
    }
}
