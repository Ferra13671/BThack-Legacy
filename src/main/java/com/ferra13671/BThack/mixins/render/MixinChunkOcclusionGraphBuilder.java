package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.events.SetOpaqueCubeEvent;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkOcclusionDataBuilder.class)
public class MixinChunkOcclusionGraphBuilder {

	@Inject(at = @At("HEAD"), method = "markClosed(Lnet/minecraft/util/math/BlockPos;)V", cancellable = true)
	private void modifyMarkClosed(BlockPos pos, CallbackInfo ci) {
		SetOpaqueCubeEvent event = new SetOpaqueCubeEvent();
		BThack.EVENT_BUS.activate(event);

		if (event.isCancelled())
			ci.cancel();
	}
}