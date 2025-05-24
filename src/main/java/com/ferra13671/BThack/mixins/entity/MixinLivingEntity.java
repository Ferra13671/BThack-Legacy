package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.Entity.JumpHeightEvent;
import com.ferra13671.BThack.events.Player.PlayerTravelEvent;
import com.ferra13671.BThack.api.Utils.Mc;
import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements Mc {

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract float getJumpBoostVelocityModifier();

    @Shadow public abstract void remove(RemovalReason reason);

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @SuppressWarnings({"ConstantValue", "UnreachableCode"})
    @Inject(method = "isBaby", at = @At("HEAD"), cancellable = true)
    public void modifyIsBaby(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == mc.player && ModuleList.babyModel.isEnabled())
            cir.setReturnValue(true);
    }

    @SuppressWarnings({"UnreachableCode", "ConstantValue"})
    @Inject(method = "getJumpVelocity(F)F", at = @At("TAIL"), cancellable = true)
    public void modifyGetJumpVelocity(float strength, CallbackInfoReturnable<Float> cir) {
        if ((Object) this != mc.player) return;
        JumpHeightEvent event = new JumpHeightEvent((float) getAttributeValue(EntityAttributes.JUMP_STRENGTH) * strength * this.getJumpVelocityMultiplier() + this.getJumpBoostVelocityModifier());

        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled())
            cir.setReturnValue(0f);
        else
            cir.setReturnValue(event.getJumpHeight());
    }

    @SuppressWarnings({"UnreachableCode", "ConstantValue"})
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
}
