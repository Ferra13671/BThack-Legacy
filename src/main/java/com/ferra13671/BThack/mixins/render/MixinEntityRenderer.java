package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.core.Client.ModuleList;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    public <S extends EntityRenderState> void modifyRenderLabelIfPresent(S state, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (ModuleList.nametags.isEnabled()) ci.cancel();
        /*
            if (ModuleList.nametags.players.getValue() && entity instanceof PlayerEntity) ci.cancel();
            if (ModuleList.nametags.items.getValue() && entity instanceof ItemEntity) ci.cancel();

         */
    }

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <T extends Entity> void shouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (ModuleList.noRender.isEnabled()) {
            if (ModuleList.noRender.fallingBlocks.getValue() && entity instanceof FallingBlockEntity)
                cir.setReturnValue(false);
            if (ModuleList.noRender.armorStands.getValue() && entity instanceof ArmorStandEntity)
                cir.setReturnValue(false);
        }
    }
}
