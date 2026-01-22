package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.events.entity.FireworkTickEvent;
import com.ferra13671.BThack.api.imixin.ModifyFireworkRocket;
import com.ferra13671.BThack.managers.Managers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public abstract class MixinFireWorkRocketEntity extends ProjectileEntity implements ModifyFireworkRocket {
    @Unique
    private boolean exploded = false;

    @Shadow private int life;

    public MixinFireWorkRocketEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void modifyTick(CallbackInfo ci) {
        if (exploded) {
            getWorld().sendEntityStatus(this, (byte)17);
            emitGameEvent(GameEvent.EXPLODE, this.getOwner());
            discard();
            ci.cancel();
        }
    }

    @SuppressWarnings("UnreachableCode")
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

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "explodeAndRemove", at = @At("HEAD"))
    public void modifyExplodeAndRemove(CallbackInfo ci) {
        Managers.FIREWORK_MANAGER.onExplode(((FireworkRocketEntity) (Object) this));
        exploded = true;
    }

    @SuppressWarnings({"UnreachableCode", "AddedMixinMembersNamePattern"})
    @Override
    public void _explodeAndRemove() {
        Managers.FIREWORK_MANAGER.onExplode(((FireworkRocketEntity) (Object) this));
        exploded = true;
    }
}
