package com.ferra13671.BThack.mixins.block;

import com.ferra13671.BThack.Core.Client.ModuleList;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class MixinAbstractBlockState {
    @Inject(method = "getModelOffset", at = @At("HEAD"), cancellable = true)
    public void modifyGetModelOffset(BlockView world, BlockPos pos, CallbackInfoReturnable<Vec3d> cir) {
        if (ModuleList.noRender != null)
            if (ModuleList.noRender.isEnabled() && ModuleList.noRender.textureRotations.getValue())
                cir.setReturnValue(new Vec3d(0, 0, 0));
    }
}
