package com.ferra13671.BThack.api.Utils.Rotate;


import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.Utils.Modules.NoRotateMathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class RotateUtils implements Mc {
    public static void rotateToEntity(Entity target) {
        rotate(rotations(target)[0], rotations(target)[1]);
    }

    public static void rotate(float yaw, float pitch) {
        float partialTicks = mc.getRenderTickCounter().getTickDelta(true);
        float oldYaw = mc.player.prevYaw;
        float oldPitch = mc.player.prevPitch;

        mc.player.setYaw(MathHelper.lerp(partialTicks, oldYaw, yaw));
        mc.player.setPitch(MathHelper.lerp(partialTicks, oldPitch, pitch));
    }

    public static void packetRotate(float yaw, float pitch) {
        Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.onGround, mc.player.horizontalCollision));
        mc.player.lastYaw = yaw;
        mc.player.lastPitch = pitch;
        mc.player.lastOnGround = mc.player.onGround;
    }

    public static float[] rotations(Entity entity) {
        return rotations(new Vec3d(entity.getX(), entity.getY(), entity.getZ()));
    }

    public static float[] rotations(BlockPos pos) {
        return rotations(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static float[] rotations(Vec3d vec3d) {
        double x = vec3d.getX() - mc.player.getX();
        double y = vec3d.getY() - mc.player.getEyeY();
        double z = vec3d.getZ() - mc.player.getZ();

        double u = MathHelper.sqrt((float)(x * x + z * z));

        float u2 = (float) (MathHelper.atan2(z, x) * (180D / Math.PI) - 90.0F);
        float u3 = (float) (-MathHelper.atan2(y, u) * (180D / Math.PI));

        return new float[]{u2, u3};
    }

    public static byte[] getCordFactorFromDirection(Entity entity) {
        return switch (getAbsDirection(entity)) {
            case 45 -> new byte[]{-1, 1};
            case 90 -> new byte[]{-1, 0};
            case 135 -> new byte[]{-1, -1};
            case 180 -> new byte[]{0, -1};
            case 225 -> new byte[]{1, -1};
            case 270 -> new byte[]{1, 0};
            case 315 -> new byte[]{1, 1};
            case 0 -> new byte[]{0, 1};
            default -> new byte[]{0, 0};
        };
    }

    public static byte[] getCordFactorFromDirection(int yaw) {
        return switch (getAbsDirection(yaw)) {
            case 45 -> new byte[]{-1, 1};
            case 90 -> new byte[]{-1, 0};
            case 135 -> new byte[]{-1, -1};
            case 180 -> new byte[]{0, -1};
            case 225 -> new byte[]{1, -1};
            case 270 -> new byte[]{1, 0};
            case 315 -> new byte[]{1, 1};
            case 0 -> new byte[]{0, 1};
            default -> new byte[]{0, 0};
        };
    }

    public static String getDirection(Entity entity) {

        return switch (NoRotateMathUtils.getNearestYawAxis(entity)) {
            case 45, -315 -> "X- Z+";
            case 90, -270 -> "X-";
            case 135, -225 -> "X- Z-";
            case 180, -180 -> "Z-";
            case 225, -135 -> "X+ Z-";
            case 270, -90 -> "X+";
            case 315, -45 -> "X+ Z+";
            case 0, 360, -360 -> "Z+";
            default -> "ERROR!";
        };
    }

    public static String getDirection(float yaw) {
        return switch (NoRotateMathUtils.getNearestYawAxis((int) yaw)) {
            case 45, -315 -> "X- Z+";
            case 90, -270 -> "X-";
            case 135, -225 -> "X- Z-";
            case 180, -180 -> "Z-";
            case 225, -135 -> "X+ Z-";
            case 270, -90 -> "X+";
            case 315, -45 -> "X+ Z+";
            case 0, 360, -360 -> "Z+";
            default -> "ERROR!";
        };
    }

    public static short getAbsDirection(Entity entity) {
        return switch (NoRotateMathUtils.getNearestYawAxis(entity)) {
            case 45, -315 -> 45;
            case 90, -270 -> 90;
            case 135, -225 -> 135;
            case 180, -180 -> 180;
            case 225, -135 -> 225;
            case 270, -90 -> 270;
            case 315, -45 -> 315;
            default -> 0;
        };
    }

    public static short getAbsDirection(int yaw) {
        return switch (NoRotateMathUtils.getNearestYawAxis(yaw)) {
            case 45, -315 -> 45;
            case 90, -270 -> 90;
            case 135, -225 -> 135;
            case 180, -180 -> 180;
            case 225, -135 -> 225;
            case 270, -90 -> 270;
            case 315, -45 -> 315;
            default -> 0;
        };
    }

    public static int getRoundedToCornersEntityRotation(Entity entity) {
        return switch (RotateUtils.getDirection(entity)) {
            case "X- Z+", "X-" -> -45;
            case "X- Z-", "Z-" -> 45;
            case "X+ Z-", "X+" -> 135;
            default -> -135;
        };
    }

    public static Direction getFacing(Entity entity) {
        float pitch = mc.player.getPitch();
        if (-45 > pitch) {
            return Direction.UP;
        }
        if (45 < pitch) {
            return Direction.DOWN;
        }

        return switch (RotateUtils.getDirection(entity)) {
            case "X- Z+", "X-" -> Direction.WEST;
            case "X- Z-", "Z-" -> Direction.NORTH;
            case "X+ Z-", "X+" -> Direction.EAST;
            default -> Direction.SOUTH;
        };
    }

    public static Direction getInvertedFacing(float yaw, float pitch, boolean pitchAlso) {
        if (pitchAlso) {
            if (-45 > pitch) {
                return Direction.DOWN;
            }
            if (45 < pitch) {
                return Direction.UP;
            }
        }

        return switch (RotateUtils.getDirection(yaw)) {
            case "X- Z+", "X-" -> Direction.EAST;
            case "X- Z-", "Z-" -> Direction.SOUTH;
            case "X+ Z-", "X+" -> Direction.WEST;
            default -> Direction.NORTH;
        };
    }

    public static Direction getInvertedFacingEntity(Entity entity) {
        float pitch = mc.player.getPitch();
        if (-45 > pitch) {
            return Direction.DOWN;
        }
        if (45 < pitch) {
            return Direction.UP;
        }

        return switch (RotateUtils.getDirection(entity)) {
            case "X- Z+", "X-" -> Direction.EAST;
            case "X- Z-", "Z-" -> Direction.SOUTH;
            case "X+ Z-", "X+" -> Direction.WEST;
            default -> Direction.NORTH;
        };
    }

    public static Vec3d getEyesPos() {
        float eyeHeight = mc.player.getEyeHeight(mc.player.getPose());
        return mc.player.getPos().add(0, eyeHeight, 0);
    }

    public static Vec3d getEyesPos(Entity entity) {
        float eyeHeight = entity.getEyeHeight(entity.getPose());
        return entity.getPos().add(0, eyeHeight, 0);
    }

    public static Vec3d getClientLookVec() {
        float yaw = mc.gameRenderer.getCamera().getYaw();
        float pitch = mc.gameRenderer.getCamera().getPitch();
        return toLookVec(yaw, pitch);
    }

    public static Vec3d toLookVec(float yaw, float pitch) {
        float radPerDeg = MathHelper.RADIANS_PER_DEGREE;
        float pi = MathHelper.PI;

        float adjustedYaw = -MathHelper.wrapDegrees(yaw) * radPerDeg - pi;
        float cosYaw = MathHelper.cos(adjustedYaw);
        float sinYaw = MathHelper.sin(adjustedYaw);

        float adjustedPitch = -MathHelper.wrapDegrees(pitch) * radPerDeg;
        float nCosPitch = -MathHelper.cos(adjustedPitch);
        float sinPitch = MathHelper.sin(adjustedPitch);

        return new Vec3d(sinYaw * nCosPitch, sinPitch, cosYaw * nCosPitch);
    }

    public static float getCameraYaw() {
        return ModuleList.noRotate.isEnabled() ? mc.player.getYaw() : mc.gameRenderer.getCamera().getYaw();
    }

    public static float getCameraPitch() {
        return ModuleList.noRotate.isEnabled() && ModuleList.noRotate.blockPitch.getValue() && !(ModuleList.elytraFlight.isEnabled() && ModuleList.elytraFlight.mode.getValue().equals("Pitch40"))
                ? mc.player.getPitch() : mc.gameRenderer.getCamera().getPitch();
    }
}
