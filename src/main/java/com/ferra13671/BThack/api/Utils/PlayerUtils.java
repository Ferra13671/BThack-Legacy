package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.mixins.accessor.IEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class PlayerUtils implements Mc {

    public static boolean isInWater() {
        return !mc.player.firstUpdate && ((IEntity) mc.player).getFluidHeight().getDouble(FluidTags.LAVA) > 0.0;
    }

    public static double calcMoveYaw() {
        float yawIn = mc.player.yaw;
        float moveForward = calcInput(mc.options.forwardKey, mc.options.backKey);
        float moveSideways = calcInput(mc.options.leftKey, mc.options.rightKey);
        float yaw = yawIn;

        if (yawIn == 0) {
            yaw = mc.player.yaw;
        }

        float strafe = 90 * moveSideways;
        strafe *= (moveForward != 0F) ? moveForward * 0.5F : 1F;

        yaw -= strafe;
        yaw -= (moveForward < 0F) ? 180 : 0;

        return Math.toRadians(yaw);
    }

    public static float calcInput(KeyBinding bindUp, KeyBinding bindDown) {
        float result = 0;
        if (bindUp.isPressed()) result += 1;
        if (bindDown.isPressed()) result -= 1;

        return result;
    }

    public static boolean canEntityBeSeen(Entity entity, Entity target) {
        return BlockUtils.hasLineOfSight(AimBotUtils.getEyesPos(entity), target.getBoundingBox().getCenter());
    }

    public static float getEntitySpeed(Entity entity) {
        return (float) (MathUtils.getDistance(entity.getPos(), new Vec3d(entity.prevX, entity.prevY, entity.prevZ)) * 20);
    }


    public static Entity createNewFakePlayer(PlayerEntity parent, String name) {
        return createNewFakePlayer(parent, new GameProfile(UUID.randomUUID(), name));
    }

    public static Entity createNewFakePlayer(PlayerEntity parent, GameProfile profile) {
        OtherClientPlayerEntity entity = new OtherClientPlayerEntity(mc.world, profile);
        entity.copyPositionAndRotation(parent);
        entity.prevYaw = entity.getYaw();
        entity.prevPitch = entity.getPitch();
        entity.headYaw = parent.headYaw;
        entity.prevHeadYaw = entity.headYaw;
        entity.bodyYaw = parent.bodyYaw;
        entity.prevBodyYaw = entity.bodyYaw;
        Byte playerModel = parent.getDataTracker()
                .get(PlayerEntity.PLAYER_MODEL_PARTS);
        entity.dataTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, playerModel);
        entity.getAttributes().setFrom(parent.getAttributes());
        entity.setPose(parent.getPose());
        entity.setHealth(parent.getHealth());
        entity.setAbsorptionAmount(parent.getAbsorptionAmount());
        // setBoundingBox(player.getBoundingBox());
        entity.getInventory().clone(parent.getInventory());
        entity.setId(new AtomicInteger(1000000).incrementAndGet());
        entity.age = 100;

        entity.unsetRemoved();
        mc.world.addEntity(entity);

        return entity;
    }

    public static void removeEntity(Entity entity) {
        mc.world.removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
        entity.setRemoved(Entity.RemovalReason.DISCARDED);
    }

    public static Vec3d getGroundPos(World world, Entity entity) {
        Vec3d vec3d = entity.getPos();

        for (double i = vec3d.y; i > -65; i--) {
            BlockPos blockPos = new BlockPos((int) vec3d.x,(int) i,(int) vec3d.z);
            BlockState state = world.getBlockState(blockPos);

            if (state != null) {
                if (state.isFullCube(world, blockPos)) {
                    return blockPos.toCenterPos().add(0, 0.5, 0);
                }
            } else {
                return entity.getPos();
            }
        }
        return entity.getPos();
    }

    public static boolean isInNether() {
        return mc.world.getRegistryKey() == World.NETHER;
    }

    public static boolean isMoving() {
        if (mc.player.input != null)
            return mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0;
        return false;
    }
}
