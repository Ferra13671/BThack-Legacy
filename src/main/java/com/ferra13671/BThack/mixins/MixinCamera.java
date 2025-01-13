package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Camera.PositionCameraEvent;
import com.ferra13671.BThack.api.Events.Camera.RotateCameraEvent;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class MixinCamera {

    @Shadow private float lastTickDelta;

    @Shadow protected abstract float clipToSpace(float desiredCameraDistance);

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;moveBy(FFF)V", ordinal = 0))
    public void modifyArgsMoveBy(Args args) {
        if (ModuleList.modifyCamera.isEnabled() && ModuleList.modifyCamera.rewriteDistance.getValue())
            args.set(0, -clipToSpace((float) ModuleList.modifyCamera.distance.getValue()));
    }

    @Inject(method = "clipToSpace", at = @At(value = "HEAD"), cancellable = true)
    public void modifyClipToSpace(float defaultDistance, CallbackInfoReturnable<Float> cir) {
        if (ModuleList.cameraClip.isEnabled()) {
            cir.setReturnValue(defaultDistance);
        }
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    public void modifyArgsSetRotationOnUpdate(Args args) {
        RotateCameraEvent event = new RotateCameraEvent(args.get(0), args.get(1));
        BThack.EVENT_BUS.activate(event);

        args.set(0, event.getYaw());
        args.set(1, event.getPitch());
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    public void modifyArgsSetPosOnUpdate(Args args) {
        PositionCameraEvent event = new PositionCameraEvent(args.get(0), args.get(1), args.get(2), lastTickDelta);
        BThack.EVENT_BUS.activate(event);

        args.set(0, event.getX());
        args.set(1, event.getY());
        args.set(2, event.getZ());
    }

    @Inject(method = "getSubmersionType", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void modifyGetSubmersionTypeWithWaterReturn(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (Client.isOptionActivated(ModuleList.noRender, ModuleList.noRender.waterFog))
            cir.setReturnValue(CameraSubmersionType.NONE);
        else cir.cancel();
    }

    @Inject(method = "getSubmersionType", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    public void modifyGetSubmersionTypeWithLavaReturn(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (Client.isOptionActivated(ModuleList.noRender, ModuleList.noRender.lavaFog))
            cir.setReturnValue(CameraSubmersionType.NONE);
        else cir.cancel();
    }

    @Inject(method = "getSubmersionType", at = @At(value = "RETURN", ordinal = 3), cancellable = true)
    public void modifyGetSubmersionTypeWithPowderSnowReturn(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (Client.isOptionActivated(ModuleList.noRender, ModuleList.noRender.powderSnowFog))
            cir.setReturnValue(CameraSubmersionType.NONE);
        else cir.cancel();
    }


    /*
    @Inject(method = "getSubmersionType", at = @At("HEAD"), cancellable = true)
    public void modifyGetSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        CameraSubmersionType returnValue = CameraSubmersionType.NONE;

        FluidState fluidState = this.area.getFluidState(this.blockPos);
        if (fluidState.isIn(FluidTags.WATER) && this.pos.y < (double)((float)this.blockPos.getY() + fluidState.getHeight(this.area, this.blockPos))) {
            returnValue = CameraSubmersionType.WATER;
        }
        Camera.Projection projection = this.getProjection();
        List<Vec3d> list = Arrays.asList(((IProjection) projection).getCenter(), projection.getBottomRight(), projection.getTopRight(), projection.getBottomLeft(), projection.getTopLeft());
        for (Vec3d vec3d : list) {
            Vec3d vec3d2 = this.pos.add(vec3d);
            BlockPos blockPos = BlockPos.ofFloored(vec3d2);
            FluidState fluidState2 = this.area.getFluidState(blockPos);
            if (fluidState2.isIn(FluidTags.LAVA)) {
                if (!(vec3d2.y <= (double)(fluidState2.getHeight(this.area, blockPos) + (float)blockPos.getY()))) continue;
                returnValue = CameraSubmersionType.LAVA;
            }
            BlockState blockState = this.area.getBlockState(blockPos);
            if (!blockState.isOf(Blocks.POWDER_SNOW)) continue;
            returnValue = CameraSubmersionType.POWDER_SNOW;
        }

        switch (returnValue) {
            case LAVA:
                if (Client.isOptionActivated(ModuleList.noRender, NoRender.lavaFog))
                    returnValue = CameraSubmersionType.NONE;
                break;
            case WATER:
                if (Client.isOptionActivated(ModuleList.noRender, NoRender.waterFog))
                    returnValue = CameraSubmersionType.NONE;
                break;
            case POWDER_SNOW:
                if (Client.isOptionActivated(ModuleList.noRender, NoRender.powderSnowFog))
                    returnValue = CameraSubmersionType.NONE;
                break;
        }
        cir.setReturnValue(returnValue);
        cir.cancel();
    }

     */

    @Inject(method = "isThirdPerson", at = @At("HEAD"), cancellable = true)
    public void modifyIsThirdPerson(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleList.freeCam.isEnabled()) cir.setReturnValue(true);
    }
}
