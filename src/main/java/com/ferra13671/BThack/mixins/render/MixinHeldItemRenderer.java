package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.Render.TransformFirstPersonEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer implements Mc {


    //TransformFirstPerson
    @Inject(method = "applySwingOffset", at = @At("HEAD"), cancellable = true)
    public void modifyApplySwingOffsetPre(MatrixStack matrices, Arm arm, float swingProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Pre(arm, matrices, TransformFirstPersonEvent.TransformType.SWING);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "applySwingOffset", at = @At("TAIL"))
    public void modifyApplySwingOffsetPost(MatrixStack matrices, Arm arm, float swingProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Post(arm, matrices, TransformFirstPersonEvent.TransformType.SWING);
        BThack.EVENT_BUS.activate(event);
    }

    @Inject(method = "applyEquipOffset", at = @At("HEAD"), cancellable = true)
    public void modifyApplyEquipOffsetPre(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Pre(arm, matrices, TransformFirstPersonEvent.TransformType.EQUIP);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "applyEquipOffset", at = @At("TAIL"))
    public void modifyApplyEquipOffsetPost(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Post(arm, matrices, TransformFirstPersonEvent.TransformType.EQUIP);
        BThack.EVENT_BUS.activate(event);
    }

    @Inject(method = "applyBrushTransformation", at = @At("HEAD"), cancellable = true)
    public void modifyApplyBrushTransformationPre(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, float equipProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Pre(arm, matrices, TransformFirstPersonEvent.TransformType.BRUSH);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "applyBrushTransformation", at = @At("TAIL"))
    public void modifyApplyBrushTransformationPost(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, float equipProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Post(arm, matrices, TransformFirstPersonEvent.TransformType.BRUSH);
        BThack.EVENT_BUS.activate(event);
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("HEAD"), cancellable = true)
    public void modifyApplyEatOrDrinkTransformationPre(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Pre(arm, matrices, TransformFirstPersonEvent.TransformType.EAT);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("TAIL"))
    public void modifyApplyEatOrDrinkTransformationPost(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Post(arm, matrices, TransformFirstPersonEvent.TransformType.EAT);
        BThack.EVENT_BUS.activate(event);
    }

    @Inject(method = "renderArmHoldingItem", at = @At("HEAD"), cancellable = true)
    public void modifyRenderArmHoldingItemPre(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Pre(arm, matrices, TransformFirstPersonEvent.TransformType.ARM);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }
    ///////////////////////////
}
