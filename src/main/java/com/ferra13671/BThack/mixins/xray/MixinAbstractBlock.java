package com.ferra13671.BThack.mixins.xray;

import com.ferra13671.BThack.impl.Modules.RENDER.Xray;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class MixinAbstractBlock {

    @Inject(at = @At("HEAD"), method = "getAmbientOcclusionLightLevel", cancellable = true)
    public void modifyGetAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (Xray.doXray) {
            cir.setReturnValue(1.0f);
            cir.cancel();
        }
    }
}
