package com.ferra13671.BThack.mixins.block;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.DataList.BlockList;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;
import com.ferra13671.BThack.impl.Modules.RENDER.Xray;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock implements Mc {

    @Inject(at = @At("HEAD"), method = "shouldDrawSide(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
    private static void onShouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction direction, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {

        if (Xray.doXray) {
            BlockState state2 = mc.world.getBlockState(pos);
            BlockList blockList = DataLists.get("Xray", BlockList.class);
            if (!blockList.values.contains(state.getBlock()))
                cir.setReturnValue(false);
            else
                cir.setReturnValue(true);

            if (!blockList.values.contains(state2.getBlock()))
                cir.setReturnValue(false);
            else
                cir.setReturnValue(true);
        }
    }

    @Inject(method = "getVelocityMultiplier", at = @At("HEAD"), cancellable = true)
    public void modifyGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        if (ModuleList.noSlow.isEnabled()) {
            if (ModuleList.noSlow.soulSand.getValue() && (Object) this == Blocks.SOUL_SAND) {
                cir.setReturnValue(Blocks.DIRT.getVelocityMultiplier());
            }
            if (ModuleList.noSlow.slime.getValue() && (Object) this == Blocks.SLIME_BLOCK) {
                cir.setReturnValue(Blocks.DIRT.getVelocityMultiplier());
            }
            if (ModuleList.noSlow.honey.getValue() && (Object) this == Blocks.HONEY_BLOCK) {
                cir.setReturnValue(Blocks.DIRT.getVelocityMultiplier());
            }
        }
    }

    @Inject(method = "getSlipperiness", at = @At("HEAD"), cancellable = true)
    public void modifyGetSlipperiness(CallbackInfoReturnable<Float> cir) {
        Block block = (Block) (Object) this;
        if (ModuleList.customFriction.isEnabled()) {
            boolean eq = DataLists.get("CustomFriction", BlockList.class).values.contains(block);
            if (ModuleList.customFriction.mode.getValue().equals("WhiteList") == eq)
                cir.setReturnValue(ModuleList.customFriction.friction.getValue().floatValue());
        }
        if (ModuleList.noSlow.isEnabled()) {
            if (ModuleList.noSlow.slime.getValue() && (Object) this == Blocks.SLIME_BLOCK) {
                cir.setReturnValue(Blocks.DIRT.getSlipperiness());
            }
            if (ModuleList.noSlow.honey.getValue() && (Object) this == Blocks.HONEY_BLOCK) {
                cir.setReturnValue(Blocks.DIRT.getSlipperiness());
            }
        }
    }
}
