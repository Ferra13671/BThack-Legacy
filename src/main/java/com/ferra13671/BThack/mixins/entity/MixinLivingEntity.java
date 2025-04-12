package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Entity.JumpHeightEvent;
import com.ferra13671.BThack.api.Events.Player.PlayerJumpEvent;
import com.ferra13671.BThack.api.Events.Player.PlayerTravelEvent;
import com.ferra13671.BThack.api.Events.Player.PlayerTraverRotEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements Mc {

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract float getJumpBoostVelocityModifier();

    @Shadow public abstract void remove(RemovalReason reason);

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract boolean isFallFlying();

    @Inject(method = "isBaby", at = @At("HEAD"), cancellable = true)
    public void modifyIsBaby(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == mc.player && ModuleList.babyModel.isEnabled())
            cir.setReturnValue(true);
    }

    @Inject(method = "getJumpVelocity(F)F", at = @At("TAIL"), cancellable = true)
    public void modifyGetJumpVelocity(float strength, CallbackInfoReturnable<Float> cir) {
        if ((Object) this != mc.player) return;
        JumpHeightEvent event = new JumpHeightEvent((float) getAttributeValue(EntityAttributes.GENERIC_JUMP_STRENGTH) * strength * this.getJumpVelocityMultiplier() + this.getJumpBoostVelocityModifier());

        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled())
            cir.setReturnValue(0f);
        else
            cir.setReturnValue(event.getJumpHeight());
    }

    @ModifyArgs(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addVelocityInternal(Lnet/minecraft/util/math/Vec3d;)V"))
    public void modifyArgsInSetVelocityOnJump(Args args) {
        if ((Object) this != mc.player) return;
        PlayerTraverRotEvent event = new PlayerTraverRotEvent(mc.player.getYaw(), mc.player.getPitch(), false);
        BThack.EVENT_BUS.activate(event);
        BThack.EVENT_BUS.activate(new PlayerJumpEvent());
        float f = event.yaw * 0.017453292F;
        args.set(0, new Vec3d((-MathHelper.sin(f) * 0.2), 0.0, (MathHelper.cos(f) * 0.2)));
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void modifyTravelPre(CallbackInfo ci) {
        if ((Object) this != mc.player) return;
        Event event = new PlayerTravelEvent();
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            move(MovementType.SELF, getVelocity());
            ci.cancel();
        }
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
    public Vec3d modifyGetRot(LivingEntity instance) {
        if (instance != mc.player) return this.getRotationVector();

        PlayerTraverRotEvent event = new PlayerTraverRotEvent(instance.yaw, instance.pitch, false);
        BThack.EVENT_BUS.activate(event);
        return this.getRotationVector(event.pitch, event.yaw);
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F", ordinal = 0))
    public float modifyGetPitch(LivingEntity instance) {
        if (instance != mc.player) return this.getPitch();

        PlayerTraverRotEvent event = new PlayerTraverRotEvent(instance.yaw, instance.pitch, false);
        BThack.EVENT_BUS.activate(event);
        return event.pitch;
    }
}
