package com.ferra13671.BThack.mixins.xray;

import com.ferra13671.BThack.impl.Modules.RENDER.Xray;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = {
		"net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer"},
	remap = false)
public class MixinSodiumDefaultFluidRenderer {


	@Inject(at = @At("HEAD"), method = "isFluidOccluded(Lnet/minecraft/class_1920;IIILnet/minecraft/class_2350;Lnet/minecraft/class_2680;Lnet/minecraft/class_3611;)Z", cancellable = true, require = 0)
	private void modifyIsFluidOccludedInOldSodium(BlockRenderView world, int x, int y, int z, Direction dir, BlockState state, Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
		if (Xray.doXray)
			cir.setReturnValue(true);
	}

	@Inject(at = @At("HEAD"),
			method = "isFluidOccluded(Lnet/minecraft/class_1920;IIILnet/minecraft/class_2350;Lnet/minecraft/class_2680;Lnet/minecraft/class_3610;)Z",
			cancellable = true,
			require = 0)
	private void onIsFluidOccluded(BlockRenderView world, int x, int y, int z, Direction dir, BlockState state, FluidState fluid, CallbackInfoReturnable<Boolean> cir) {
		if (Xray.doXray)
			cir.setReturnValue(true);
	}
}