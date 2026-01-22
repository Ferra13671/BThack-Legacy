package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.api.utils.MathUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class MixinBlockEntityRenderDispatcher implements Mc {

    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"), cancellable = true)
    public <E extends BlockEntity> void modifyBlockEntityRender(E blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (ModuleList.noRender.isEnabled()) {
            double distance = MathUtils.getDistance(mc.player.getPos(), blockEntity.getPos().toCenterPos());
            if (blockEntity instanceof ChestBlockEntity && ModuleList.noRender.chestRender.getValue())
                if (distance > ModuleList.noRender.chestRadius.getValue())
                    ci.cancel();
            if (blockEntity instanceof ShulkerBoxBlockEntity && ModuleList.noRender.shulkerRender.getValue())
                if (distance > ModuleList.noRender.shulkerRadius.getValue())
                    ci.cancel();
            if (blockEntity instanceof EnchantingTableBlockEntity && ModuleList.noRender.eTableRender.getValue())
                if (distance > ModuleList.noRender.eTableRadius.getValue())
                    ci.cancel();
        }
    }
}
