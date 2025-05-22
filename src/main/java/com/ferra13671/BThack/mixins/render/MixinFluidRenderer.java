package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.impl.Modules.Render.Xray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.util.math.Direction;

@Mixin(FluidRenderer.class)
public class MixinFluidRenderer {

	@Inject(at = @At("HEAD"), method = "isSideCovered", cancellable = true)
	private static void onIsSideCovered(Direction direction, float f, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
		if (Xray.doXray)
			cir.setReturnValue(false);
	}
}