package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.DataList.BlockList;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;
import com.ferra13671.BThack.impl.Modules.RENDER.Xray;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = {
		"net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache"},
	remap = false)
public class MixinSodiumBlockOcclusionCache implements Mc {


	@Inject(at = @At("HEAD"), method = "shouldDrawSide", cancellable = true)
	public void modifyShouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
		if (Xray.doXray) {
			BlockState state2 = mc.world.getBlockState(pos);
			if (!DataLists.get("Xray", BlockList.class).values.contains(state.getBlock()))
				cir.setReturnValue(false);
			else
				cir.setReturnValue(true);

			if (!DataLists.get("Xray", BlockList.class).values.contains(state2.getBlock()))
				cir.setReturnValue(false);
			else
				cir.setReturnValue(true);
		}
	}
}