package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends Entity implements Mc {

    public MixinPlayerEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    public Vec3d modifySetVelocityOnAttack(Vec3d vec3d) {
        if ((Object) this == mc.player && ModuleList.keepSprint.isEnabled()) return getVelocity();
        else return vec3d;
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V"))
    public boolean modifySetSprintingOnAttack(boolean sprinting) {
        if ((Object) this == mc.player && ModuleList.keepSprint.isEnabled()) return isSprinting();
        else return sprinting;
    }
}
