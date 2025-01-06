package com.ferra13671.BThack.mixins.xray;

import com.ferra13671.BThack.impl.Modules.RENDER.Xray;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = {
	"me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer",
	"me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer"},
	remap = false)
public class MixinSodiumFluidRenderer {

	@Inject(at = @At("HEAD"), method = "isSideExposed", cancellable = true)
	private void isSideExposed(BlockRenderView world, int x, int y, int z, Direction dir, float height, CallbackInfoReturnable<Boolean> cir) {
		if (Xray.doXray)
			cir.setReturnValue(false);
	}
}