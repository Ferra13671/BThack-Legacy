package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.Entity.FireworkTickEvent;
import com.ferra13671.BThack.api.Events.Player.PlayerTraverRotEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FireworkRocketEntity.class)
public abstract class MixinFireWorkRocketEntity extends ProjectileEntity {
    @Unique
    private boolean exploded = false;

    @Shadow private int life;

    @Shadow @Nullable private LivingEntity shooter;

    @Shadow protected abstract void explode();

    public MixinFireWorkRocketEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void modifyTick(CallbackInfo ci) {
        if (exploded) {
            getWorld().sendEntityStatus(this, (byte)17);
            emitGameEvent(GameEvent.EXPLODE, this.getOwner());
            explode();
            discard();
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;updateRotation()V", shift = At.Shift.AFTER), cancellable = true)
    public void modifyTickAfterUpdateRotation(CallbackInfo ci) {
        FireworkRocketEntity rocketEntity = ((FireworkRocketEntity) (Object) this);
        FireworkTickEvent event = new FireworkTickEvent(rocketEntity);
        BThack.EVENT_BUS.activate(event);
        Managers.FIREWORK_MANAGER.updateFireWorkTick();
        if (event.isCancelled()) {
            ci.cancel();
            if (life == 0 && !rocketEntity.isSilent())
                getWorld().playSound(null, rocketEntity.getX(), rocketEntity.getY(), rocketEntity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0f, 1.0f);
            ++life;
            if (getWorld().isClient && life % 2 < 2)
                getWorld().addParticle(ParticleTypes.FIREWORK, rocketEntity.getX(), rocketEntity.getY(), rocketEntity.getZ(), getWorld().random.nextGaussian() * 0.05, -rocketEntity.getVelocity().y * 0.5, getWorld().random.nextGaussian() * 0.05);
        }
    }


    @ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    public void modifySetVelocity(Args args) {
        if (shooter != MinecraftClient.getInstance().player) return;

        PlayerTraverRotEvent event = new PlayerTraverRotEvent(shooter.yaw, shooter.pitch, true);
        BThack.EVENT_BUS.activate(event);

        Vec3d vec3d = shooter.getRotationVector(event.pitch, event.yaw);
        Vec3d vec3d2 = shooter.getVelocity();

        args.set(0, vec3d2.add(vec3d.x * 0.1 + (vec3d.x * 1.5 - vec3d2.x) * 0.5, vec3d.y * 0.1 + (vec3d.y * 1.5 - vec3d2.y) * 0.5, vec3d.z * 0.1 + (vec3d.z * 1.5 - vec3d2.z) * 0.5));
    }

    @Inject(method = "explodeAndRemove", at = @At("HEAD"))
    public void modifyExplodeAndRemove(CallbackInfo ci) {
        Managers.FIREWORK_MANAGER.onExplode(((FireworkRocketEntity) (Object) this));
        exploded = true;
    }
}
