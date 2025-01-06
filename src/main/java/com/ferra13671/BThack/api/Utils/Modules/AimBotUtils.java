package com.ferra13671.BThack.api.Utils.Modules;


import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class AimBotUtils implements Mc {
    public static void rotateToEntity(Entity target) {
        rotate(rotations(target)[0], rotations(target)[1]);
    }

    public static void rotate(float yaw, float pitch) {
        float partialTicks = mc.getTickDelta();
        float oldYaw = mc.player.prevYaw;
        float oldPitch = mc.player.prevPitch;

        mc.player.setYaw(MathHelper.lerp(partialTicks, oldYaw, yaw));
        mc.player.setPitch(MathHelper.lerp(partialTicks, oldPitch, pitch));
    }


    public static void packetRotateToEntity(Entity target) {
        float yaw = rotations(target)[0];
        float pitch = rotations(target)[1];
        packetRotate(yaw, pitch);
    }

    public static void packetRotate(float yaw, float pitch) {
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.onGround));
        mc.player.lastYaw = yaw;
        mc.player.lastPitch = pitch;
        mc.player.lastOnGround = mc.player.onGround;
    }



    public static float[] rotations(Entity entity) {
        double x = entity.getX() - mc.player.getX();
        double y = entity.getY() - (mc.player.getY() + mc.player.getStandingEyeHeight() - 0.7);
        double z = entity.getZ() - mc.player.getZ();

        double u = Math.sqrt(x * x + z * z);

        float u2 = (float) (MathHelper.atan2(z, x) * (180D / Math.PI) - 90.0F);
        float u3 = (float) (-MathHelper.atan2(y, u) * (180D / Math.PI));

        return new float[]{u2, u3};
    }

    public static float[] rotations(BlockPos pos) {
        Vec3d eyePos = mc.player.getEyePos();
        double x = pos.getX() - eyePos.getX();
        double y = pos.getY() - eyePos.getY();
        double z = pos.getZ() - eyePos.getZ();

        double u = Math.sqrt(x * x + z * z);

        float u2 = (float) (MathHelper.atan2(z, x) * (180D / Math.PI) - 90.0F);
        float u3 = (float) (-MathHelper.atan2(y, u) * (180D / Math.PI));

        return new float[]{u2, u3};
    }

    public static float[] rotations(Vec3d to) {
        Vec3d eyePos = mc.player.getEyePos();
        double x = to.getX() - eyePos.getX();
        double y = to.getY() - eyePos.getY();
        double z = to.getZ() - eyePos.getZ();

        double u = Math.sqrt(x * x + z * z);

        float u2 = (float) (MathHelper.atan2(z, x) * (180D / Math.PI) - 90.0F);
        float u3 = (float) (-MathHelper.atan2(y, u) * (180D / Math.PI));

        return new float[]{u2, u3};
    }

    public static Vec3d movementInputToVelocity(float yaw, Vec3d movementInput, float speed) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        }
        Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
        float f = MathHelper.sin(yaw * MathHelper.RADIANS_PER_DEGREE);
        float g = MathHelper.cos(yaw * MathHelper.RADIANS_PER_DEGREE);
        return new Vec3d(vec3d.x * (double) g - vec3d.z * (double) f, vec3d.y, vec3d.z * (double) g + vec3d.x * (double) f);
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

        return switch (NoRotateMathUtils.rotateYawMath(entity)) {
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

        return switch (NoRotateMathUtils.rotateYawMath(yaw)) {
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
        return switch (NoRotateMathUtils.rotateYawMath(entity)) {
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
        return switch (NoRotateMathUtils.rotateYawMath(yaw)) {
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
        int yaw;

        switch (AimBotUtils.getDirection(entity)) {
            case "X- Z+":
            case  "X-":
                yaw = -45;
                break;
            case "X- Z-":
            case "Z-":
                yaw = 45;
                break;
            case "X+ Z-":
            case "X+":
                yaw = 135;
                break;
            case "X+ Z+":
            case "Z+":
            default:    
                yaw = -135;
                break;
        }
        
        return yaw;
    }

    public static int getRoundedToStraightEntityRotation(Entity entity) {
        int yaw;

        switch (AimBotUtils.getDirection(entity)) {
            case "X- Z+":
            case  "X-":
                yaw = 90;
                break;
            case "X- Z-":
            case "Z-":
                yaw = 180;
                break;
            case "X+ Z-":
            case "X+":
                yaw = 270;
                break;
            case "X+ Z+":
            case "Z+":
            default:
                yaw = 0;
                break;
        }

        return yaw;
    }

    public static Direction getFacing(Entity entity) {
        float pitch = mc.player.getPitch();
        if (-45 > pitch) {
            return Direction.UP;
        }
        if (45 < pitch) {
            return Direction.DOWN;
        }

        switch (AimBotUtils.getDirection(entity)) {
            case "X- Z+":
            case  "X-":
                return Direction.WEST;
            case "X- Z-":
            case "Z-":
                return Direction.NORTH;
            case "X+ Z-":
            case "X+":
                return Direction.EAST;
            case "X+ Z+":
            case "Z+":
            default:
                return Direction.SOUTH;
        }
    }

    public static Direction getFacing(float yaw, float pitch, boolean pitchAlso) {
        if (pitchAlso) {
            if (-45 > pitch) {
                return Direction.UP;
            }
            if (45 < pitch) {
                return Direction.DOWN;
            }
        }

        return switch (AimBotUtils.getDirection(yaw)) {
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

        return switch (AimBotUtils.getDirection(yaw)) {
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

        return switch (AimBotUtils.getDirection(entity)) {
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
}
