package com.ferra13671.BThack.api.Utils;


import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Modules.NoRotateMathUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class RotateUtils implements Mc {

    public static float[] rotations(Entity entity) {
        double x = entity.getX() - mc.player.getX();
        double y = entity.getY() - (mc.player.getY() + mc.player.getStandingEyeHeight() - 0.7);
        double z = entity.getZ() - mc.player.getZ();

        double u = MathHelper.sqrt((float)(x * x + z * z));

        float u2 = (float) (MathHelper.atan2(z, x) * (180D / Math.PI) - 90.0F);
        float u3 = (float) (-MathHelper.atan2(y, u) * (180D / Math.PI));

        return new float[]{u2, u3};
    }

    public static float[] rotations(Vec3d vec3d) {
        double x = vec3d.getX() - mc.player.getX();
        double y = vec3d.getY() - (mc.player.getY() + mc.player.getStandingEyeHeight() - 0.7);
        double z = vec3d.getZ() - mc.player.getZ();

        double u = MathHelper.sqrt((float)(x * x + z * z));

        float u2 = (float) (MathHelper.atan2(z, x) * (180D / Math.PI) - 90.0F);
        float u3 = (float) (-MathHelper.atan2(y, u) * (180D / Math.PI));

        return new float[]{u2, u3};
    }

    public static String getDirection(PlayerEntity entity) {
        String look = "ERROR!";

        switch (NoRotateMathUtils.rotateYawMath(entity)) {
            case 45:
            case -315:
                look = "X- Z+";
                break;
            case 90:
            case -270:
                look = "X-";
                break;
            case 135:
            case -225:
                look = "X- Z-";
                break;
            case 180:
            case -180:
                look = "Z-";
                break;
            case 225:
            case -135:
                look = "X+ Z-";
                break;
            case 270:
            case -90:
                look = "X+";
                break;
            case 315:
            case -45:
                look = "X+ Z+";
                break;
            case 0:
            case 360:
            case -360:
                look = "Z+";
                break;
        }

        return look;
    }

    public static Vec3d getClientLookVec(float partialTicks) {
        float yaw = mc.gameRenderer.getCamera().getYaw();
        float pitch = mc.gameRenderer.getCamera().getPitch();
        return toLookVec(yaw, pitch);
    }

    public static Vec3d getEyesPos() {
        ClientPlayerEntity player = mc.player;
        float eyeHeight = player.getEyeHeight(player.getPose());
        return player.getPos().add(0, eyeHeight, 0);
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
}
