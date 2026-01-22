package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.core.client.ModuleList;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class MixinArmorFeatureRenderer {
    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    public <S extends BipedEntityRenderState, A extends BipedEntityModel<S>> void modifyRenderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, EquipmentSlot slot, int light, A armorModel, CallbackInfo ci) {
        if (ModuleList.noRender.isEnabled() && ModuleList.noRender.armor.getValue())
            ci.cancel();
    }
}
