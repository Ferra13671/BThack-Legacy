package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.core.client.ModuleList;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    public <T extends Entity> void modifyGetDisplayName(T entity, CallbackInfoReturnable<Text> cir) {
        if (ModuleList.nametags.isEnabled()) {
            if (ModuleList.nametags.players.getValue() && entity instanceof PlayerEntity) cir.setReturnValue(null);
            if (ModuleList.nametags.items.getValue() && entity instanceof ItemEntity) cir.setReturnValue(null);
        }
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
