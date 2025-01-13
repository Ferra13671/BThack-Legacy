package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.Entity.FireworkTickEvent;
import com.ferra13671.BThack.api.Events.Player.PlayerTraverRotEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public class MixinFireWorkRocketEntity implements Mc {

    @Shadow public int life;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;updateRotation()V", shift = At.Shift.AFTER), cancellable = true)
    public void modifyRocketTick(CallbackInfo ci) {
        FireworkRocketEntity rocketEntity = ((FireworkRocketEntity) (Object) this);
        FireworkTickEvent event = new FireworkTickEvent(rocketEntity);
        BThack.EVENT_BUS.activate(event);
        Managers.FIREWORK_MANAGER.updateFireWorkTick();
        if (event.isCancelled()) {
            ci.cancel();
            if (life == 0 && !rocketEntity.isSilent())
                mc.world.playSound(null, rocketEntity.getX(), rocketEntity.getY(), rocketEntity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0f, 1.0f);
            ++life;
            if (mc.world.isClient && life % 2 < 2)
                mc.world.addParticle(ParticleTypes.FIREWORK, rocketEntity.getX(), rocketEntity.getY(), rocketEntity.getZ(), mc.world.random.nextGaussian() * 0.05, -rocketEntity.getVelocity().y * 0.5, mc.world.random.nextGaussian() * 0.05);
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
    public Vec3d modifyGetRotationVector(LivingEntity instance) {
        if (instance != mc.player) return instance.getRotationVector();

        PlayerTraverRotEvent event = new PlayerTraverRotEvent(instance.yaw, instance.pitch, true);
        BThack.EVENT_BUS.activate(event);
        return instance.getRotationVector(event.pitch, event.yaw);
    }

    @Inject(method = "explodeAndRemove", at = @At("HEAD"))
    public void modifyExplodeAndRemove(CallbackInfo ci) {
        Managers.FIREWORK_MANAGER.onExplode(((FireworkRocketEntity) (Object) this));
    }
}
