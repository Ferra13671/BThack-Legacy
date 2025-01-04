package com.ferra13671.BThack.api.Utils.Modules;

import net.minecraft.entity.Entity;

public final class NoRotateMathUtils {
    private static double checkYaw1;
    private static double checkYaw2;
    private static double checkPitch1;
    private static double checkPitch2;

    public static int rotateYawMath(Entity entity) {
        return rotateYawMath(entity.getYaw());
    }

    public static int rotateYawMath(float yaw) {
        int CorrectlyYaw = 0;
        boolean isNegative;

        yaw = yaw / 360;
        if (yaw < 0) {
            isNegative = true;
            yaw = -yaw;
        } else
            isNegative = false;
        int yawint = (int)yaw;
        yaw = yaw - yawint;
        if (yaw <= 0.125 && yaw >= 0) {
            checkYaw1 = 0.125 - yaw;
            if (yaw > checkYaw1)
                CorrectlyYaw = checkIsNegative(isNegative, 45);
        }
        if (yaw <= 0.25 && yaw >= 0.125) {
            checkYawRotate(0.25, 0.125, yaw);
            if (checkYaw1 > checkYaw2)
                CorrectlyYaw = checkIsNegative(isNegative, 45);
            else
                CorrectlyYaw = checkIsNegative(isNegative, 90);
        }
        if (yaw <= 0.375 && yaw >= 0.25) {
            checkYawRotate(0.375, 0.25, yaw);
            if (checkYaw1 > checkYaw2)
                CorrectlyYaw = checkIsNegative(isNegative, 90);
            else
                CorrectlyYaw = checkIsNegative(isNegative, 135);
        }
        if (yaw <= 0.5 && yaw >= 0.375) {
            checkYawRotate(0.5, 0.375, yaw);
            if (checkYaw1 > checkYaw2)
                CorrectlyYaw = checkIsNegative(isNegative, 135);
            else
                CorrectlyYaw = checkIsNegative(isNegative, 180);
        }
        if (yaw <= 0.625 && yaw >= 0.5) {
            checkYawRotate(0.625, 0.5, yaw);
            if (checkYaw1 > checkYaw2)
                CorrectlyYaw = checkIsNegative(isNegative, 180);
            else
                CorrectlyYaw = checkIsNegative(isNegative, 225);
        }
        if (yaw <= 0.75 && yaw >= 0.625) {
            checkYawRotate(0.75, 0.625, yaw);
            if (checkYaw1 > checkYaw2)
                CorrectlyYaw = checkIsNegative(isNegative, 225);
            else
                CorrectlyYaw = checkIsNegative(isNegative, 270);
        }
        if (yaw <= 0.875 && yaw >= 0.75) {
            checkYawRotate(0.875, 0.75, yaw);
            if (checkYaw1 > checkYaw2)
                CorrectlyYaw = checkIsNegative(isNegative, 270);
            else
                CorrectlyYaw = checkIsNegative(isNegative, 315);
        }
        if (yaw >= 0.875) {
            checkYaw1 = yaw - 0.875;
            if (checkYaw1 <= 0.0625)
                CorrectlyYaw = checkIsNegative(isNegative, 315);
            else
                CorrectlyYaw = checkIsNegative(isNegative, 360);
        }

        return CorrectlyYaw;
    }

    public static int RotatePitchMath(Entity entity) {
        int CorrectlyPitch = 0;
        boolean isNegative2;

        double pitch = entity.getPitch();
        if (pitch < 0) {
            isNegative2 = true;
            pitch = -pitch;
        } else
            isNegative2 = false;
        if (pitch <= 90 && pitch >= 45) {
            checkPitchRotate(90, 45, pitch);
            if (checkPitch1 > checkPitch2)
                CorrectlyPitch = checkIsNegative(isNegative2, 45);
            else
                CorrectlyPitch = checkIsNegative(isNegative2, 90);
        }
        if (pitch <= 45 && pitch >= 0) {
            checkPitch1 = 45 - pitch;
            if (checkPitch1 <= pitch)
                CorrectlyPitch = checkIsNegative(isNegative2, 45);
        }

        return CorrectlyPitch;
    }

    protected static int checkIsNegative(boolean a, int b) {
        if (a) {
            return -b;
        } else {
            return b;
        }
    }

    protected static void checkYawRotate(double maxValue, double minValue, double yaw) {
        checkYaw1 = maxValue - yaw;
        checkYaw2 = yaw - minValue;
    }

    protected static void checkPitchRotate(double maxValue, double minValue, double pitch) {
        checkPitch1 = maxValue - pitch;
        checkPitch2 = pitch - minValue;
    }
}
