package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Entity.SetVelocityEvent;
import com.ferra13671.BThack.api.Events.Player.VelocityUpdateEvent;
import com.ferra13671.BThack.api.Events.Player.ChangePlayerLookEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Modules.NoRotateMathUtils;
import com.ferra13671.MegaEvents.Base.Event;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.fluid.FlowableFluid.FALLING;

@Mixin(Entity.class)
public abstract class MixinEntity implements Mc {


    @Shadow public Vec3d velocity;

    @Shadow public abstract Text getDisplayName();

    @Shadow public float yaw;

    @Shadow protected abstract void setFlag(int index, boolean value);

    @Shadow public abstract Box getBoundingBox();

    @Shadow public abstract boolean isRegionUnloaded();

    @Shadow public abstract boolean isPushedByFluids();

    @Shadow public abstract World getWorld();

    @Shadow public abstract Vec3d getVelocity();

    @Shadow public abstract void setVelocity(Vec3d velocity);

    @Shadow protected Object2DoubleMap<TagKey<Fluid>> fluidHeight;

    @Shadow
    private static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        return null;
    }

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

    @Inject(method = "setYaw", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void modifySetYaw(float yaw, CallbackInfo ci) {
        if ((Object) this != mc.player) return;
        if (ModuleList.noRotate.isEnabled()) {
            this.yaw = NoRotateMathUtils.getNearestYawAxis(mc.player);
            ci.cancel();
        }
    }

    @Inject(method = "setPitch", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void modifySetPitch(float pitch, CallbackInfo ci) {
        if ((Object) this != mc.player) return;
        if (ModuleList.noRotate.isEnabled()) {
            if (ModuleList.noRotate.blockPitch.getValue()) {
                mc.player.pitch = NoRotateMathUtils.getNearestPitchAxis(mc.player);
                ci.cancel();
            }
        }
    }

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    public void modifyChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        Event event = new ChangePlayerLookEvent(cursorDeltaX, cursorDeltaY);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "setFlag", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void modifySetFlag(int index, boolean value, CallbackInfo ci) {
        if ((Object) this != mc.player) return;
        if (ModuleList.elytraFlight.isEnabled()) {
            if (index == 7 && !value) {
                this.setFlag(7, true);
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                ci.cancel();
            }
        }
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void modifyPushAwayFrom(Entity entity, CallbackInfo ci) {
        if ((Object) this == mc.player)
            if (ModuleList.noPush.isEnabled())
                if (ModuleList.noPush.entities.getValue())
                    ci.cancel();
    }

    @Inject(method = "updateVelocity", at = @At(value = "HEAD"), cancellable = true)
    private void modifyUpdateVelocity(float speed, Vec3d movementInput, CallbackInfo ci) {
        if ((Object) this == mc.player) {
            VelocityUpdateEvent event = new VelocityUpdateEvent(movementInput, speed, movementInputToVelocity(movementInput, speed, mc.player.getYaw()));
            BThack.EVENT_BUS.activate(event);
            if (event.isCancelled()) {
                ci.cancel();
                mc.player.setVelocity(mc.player.getVelocity().add(event.getVelocity()));
            }
        }
    }

    @Inject(method = "updateMovementInFluid", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void modifyUpdateMovementInFluid(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this != mc.player) return;

        cir.cancel();

        if (isRegionUnloaded()) {
            cir.setReturnValue(false);
        } else {
            Box box = getBoundingBox().contract(0.001);
            int i = MathHelper.floor(box.minX);
            int j = MathHelper.ceil(box.maxX);
            int k = MathHelper.floor(box.minY);
            int l = MathHelper.ceil(box.maxY);
            int m = MathHelper.floor(box.minZ);
            int n = MathHelper.ceil(box.maxZ);
            double d = 0.0;
            boolean bl = isPushedByFluids();
            boolean bl2 = false;
            Vec3d vec3d = Vec3d.ZERO;
            int o = 0;
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for(int p = i; p < j; ++p) {
                for(int q = k; q < l; ++q) {
                    for(int r = m; r < n; ++r) {
                        mutable.set(p, q, r);
                        FluidState fluidState = getWorld().getFluidState(mutable);
                        if (fluidState.isIn(tag)) {
                            double e = ((float)q + fluidState.getHeight(getWorld(), mutable));
                            if (e >= box.minY) {
                                bl2 = true;
                                d = Math.max(e - box.minY, d);
                                if (bl) {
                                    Vec3d vec3d2 = getFluidVelocity(getWorld(), mutable, fluidState);
                                    if (d < 0.4) {
                                        vec3d2 = vec3d2.multiply(d);
                                    }

                                    vec3d = vec3d.add(vec3d2);
                                    ++o;
                                }
                            }
                        }
                    }
                }
            }

            if (vec3d.length() > 0.0) {
                if (o > 0) {
                    vec3d = vec3d.multiply(1.0 / (double)o);
                }

                Vec3d vec3d3 = getVelocity();
                vec3d = vec3d.multiply(speed);
                if (Math.abs(vec3d3.x) < 0.003 && Math.abs(vec3d3.z) < 0.003 && vec3d.length() < 0.0045000000000000005) {
                    vec3d = vec3d.normalize().multiply(0.0045000000000000005);
                }

                setVelocity(getVelocity().add(vec3d));
            }

            fluidHeight.put(tag, d);
            cir.setReturnValue(bl2);
        }
    }

    @Unique
    public Vec3d getFluidVelocity(BlockView world, BlockPos pos, FluidState state) {
        if (ModuleList.noPush.isEnabled()) {
            if (state.getFluid() instanceof FlowableFluid fluid) {
                if (ModuleList.noPush.liquids.getValue()) {
                    Vec3d vec3d = Vec3d.ZERO;
                    if (state.get(FALLING)) {
                        BlockPos.Mutable mutable = new BlockPos.Mutable();

                        for (Direction direction2 : Direction.Type.HORIZONTAL) {
                            mutable.set(pos, direction2);
                            if (!fluid.isFlowBlocked(world, mutable, direction2) && !fluid.isFlowBlocked(world, mutable.up(), direction2))
                                continue;
                            vec3d = vec3d.normalize().add(0.0, -6.0, 0.0);
                            break;
                        }
                    }
                    return vec3d.normalize();
                }
            } else {
                return state.getVelocity(world, pos);
            }
        }
        return state.getVelocity(world, pos);
    }
}
