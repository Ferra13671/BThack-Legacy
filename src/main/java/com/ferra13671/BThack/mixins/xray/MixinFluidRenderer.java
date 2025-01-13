package com.ferra13671.BThack.mixins.xray;

import com.ferra13671.BThack.impl.Modules.RENDER.Xray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

@Mixin(FluidRenderer.class)
public class MixinFluidRenderer {

	@Inject(at = @At("HEAD"), method = "isSideCovered(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;FLnet/minecraft/block/BlockState;)Z", cancellable = true)
	private static void onIsSideCovered(BlockView world, BlockPos pos, Direction side, float maxDeviation, BlockState neighboringBlockState, CallbackInfoReturnable<Boolean> cir) {
		if (Xray.doXray)
			cir.setReturnValue(false);
	}
}