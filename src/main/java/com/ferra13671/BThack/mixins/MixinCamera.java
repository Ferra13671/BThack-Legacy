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

    @Inject(method = "isThirdPerson", at = @At("HEAD"), cancellable = true)
    public void modifyIsThirdPerson(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleList.freeCam.isEnabled()) cir.setReturnValue(true);
    }
}
