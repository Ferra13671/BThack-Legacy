package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.Entity.SetVelocityEvent;
import com.ferra13671.BThack.events.Player.ChangePlayerLookEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Modules.NoRotateMathUtils;
import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity implements Mc {


    @Shadow public Vec3d velocity;

    @Shadow public abstract Text getDisplayName();

    @Shadow public float yaw;

    @Shadow public abstract World getWorld();

    @Shadow public abstract Vec3d getVelocity();

    @Shadow public abstract void setVelocity(Vec3d velocity);

    @Shadow public float pitch;

    @Inject(method = "setVelocity(DDD)V", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void modifySetVelocity(double x, double y, double z, CallbackInfo ci) {
        if ((Object) this != mc.player) return;
        SetVelocityEvent event = new SetVelocityEvent(new Vec3d(x, y, z));

        BThack.EVENT_BUS.activate(event);

        if (!event.isCancelled())
            this.velocity = event.getVelocity();
        ci.cancel();
    }

    @Inject(method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void modifySetVelocityVec3d(Vec3d velocity, CallbackInfo ci) {
        if ((Object) this != mc.player) return;
        SetVelocityEvent event = new SetVelocityEvent(velocity);

        BThack.EVENT_BUS.activate(event);

        if (!event.isCancelled())
            this.velocity = event.getVelocity();
        ci.cancel();
    }

    @Inject(method = "isSprinting", at = @At("HEAD"), cancellable = true)
    public void modifyIsSprinting(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleList.elytraFlight.isEnabled() && ModuleList.elytraFlight.mode.getValue().equals("Bounce") && (ModuleList.elytraFlight.alwaysPress.getValue().equals("Sprint") || ModuleList.elytraFlight.alwaysPress.getValue().equals("Multi")))
            cir.setReturnValue(true);
    }

    @Inject(method = "setYaw", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void modifySetYaw(float yaw, CallbackInfo ci) {
        if ((Object) this != mc.player) return;
        if (ModuleList.noRotate.isEnabled()) {
            ci.cancel();
            this.yaw = NoRotateMathUtils.getNearestYawAxis(mc.player);
        }
    }

    @Inject(method = "setPitch", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void modifySetPitch(float pitch, CallbackInfo ci) {
        if ((Object) this != mc.player) return;
        if (ModuleList.noRotate.isEnabled() && ModuleList.noRotate.blockPitch.getValue()) {
            if (ModuleList.elytraFlight.isEnabled() && ModuleList.elytraFlight.mode.getValue().equals("Pitch40")) return;
            ci.cancel();
            this.pitch = NoRotateMathUtils.getNearestPitchAxis(mc.player);
        }
    }

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    public void modifyChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        Event event = new ChangePlayerLookEvent(cursorDeltaX, cursorDeltaY);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void modifyPushAwayFrom(Entity entity, CallbackInfo ci) {
        if ((Object) this == mc.player)
            if (ModuleList.noPush.isEnabled())
                if (ModuleList.noPush.entities.getValue())
                    ci.cancel();
    }

    @ModifyVariable(method = "updateMovementInFluid", at = @At("STORE"), ordinal = 1)
    public Vec3d modifyGetFluidStateVelocityOnUpdateMovementInFluid(Vec3d vec3d) {
        if ((Object) this == mc.player && ModuleList.noPush.isEnabled() && ModuleList.noPush.liquids.getValue())
            return new Vec3d(0, vec3d.getY(), 0);
        else return vec3d;
    }
}
