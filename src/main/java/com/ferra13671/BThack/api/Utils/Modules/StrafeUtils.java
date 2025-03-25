package com.ferra13671.BThack.api.Utils.Modules;


import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.RotateUtils;

public final class StrafeUtils implements Mc {

    public static float getPlayerYawOnInput() {
        float yaw = RotateUtils.getCameraYaw();
        float strafe = 45;
        if (mc.player.input.movementForward < 0) {
            strafe = -45;
            yaw += 180;
        }
        if (mc.player.input.movementSideways > 0) {
            yaw -= strafe;
            if (mc.player.input.movementForward == 0) {
                yaw -= 45;
            }
        } else if (mc.player.input.movementSideways < 0) {
            yaw += strafe;
            if (mc.player.input.movementForward == 0) {
                yaw += 45;
            }
        }
        return yaw;
    }

    public static float getPlayerYawOnKeybindings() {
        float yaw = RotateUtils.getCameraYaw();
        float strafe = 45;
        if (mc.options.backKey.isPressed()) {
            strafe = -45;
            yaw += 180;
        }
        if (mc.options.leftKey.isPressed()) {
            yaw -= strafe;
            if (!mc.options.forwardKey.isPressed() && !mc.options.backKey.isPressed()) {
                yaw -= 45;
            }
        } else if (mc.options.rightKey.isPressed()) {
            yaw += strafe;
            if (!mc.options.forwardKey.isPressed() && !mc.options.backKey.isPressed()) {
                yaw += 45;
            }
        }
        return yaw;
    }

    public static double[] getMoveFactors() {
        return getMoveFactors(StrafeUtils.getPlayerYawOnInput());
    }

    public static double[] getMoveFactors(float yaw) {
        yaw = (float) Math.toRadians(yaw);
        return new double[]{-Math.sin(yaw), Math.cos(yaw)};
    }

    public static double[] getMoveFactors(double speed) {
        double[] move = getMoveFactors();
        move[0] = move[0] * speed;
        move[1] = move[1] * speed;
        return move;
    }

    public static double[] getMoveFactors(double speed, float yaw) {
        yaw = (float) Math.toRadians(yaw);
        double[] move = getMoveFactors(yaw);
        move[0] = move[0] * speed;
        move[1] = move[1] * speed;
        return move;
    }
}
