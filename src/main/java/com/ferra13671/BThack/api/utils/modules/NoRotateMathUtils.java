package com.ferra13671.BThack.api.utils.modules;

import com.ferra13671.BThack.api.utils.MathUtils;
import net.minecraft.entity.Entity;

public final class NoRotateMathUtils {

    public static int getNearestYawAxis(Entity entity) {
        return getNearestYawAxis((int) entity.getYaw());
    }

    public static int getNearestYawAxis(int yaw) {
        boolean invert = false;

        if (yaw < 0) {
            invert = true;
            yaw *= -1;
        }

        yaw = yaw % 360;

        if (MathUtils.hasInRange(yaw, 0, 45))
            return invertIfNeed(invert, MathUtils.nearest(yaw, 0, 45));
        if (MathUtils.hasInRange(yaw, 45, 90))
            return invertIfNeed(invert, MathUtils.nearest(yaw, 45, 90));
        if (MathUtils.hasInRange(yaw, 90, 135))
            return invertIfNeed(invert, MathUtils.nearest(yaw, 90, 135));
        if (MathUtils.hasInRange(yaw, 135, 180))
            return invertIfNeed(invert, MathUtils.nearest(yaw, 135, 180));
        if (MathUtils.hasInRange(yaw, 180, 225))
            return invertIfNeed(invert, MathUtils.nearest(yaw, 180, 225));
        if (MathUtils.hasInRange(yaw, 225, 270))
            return invertIfNeed(invert, MathUtils.nearest(yaw, 225, 270));
        if (MathUtils.hasInRange(yaw, 270, 315))
            return invertIfNeed(invert, MathUtils.nearest(yaw, 270, 315));
        if (MathUtils.hasInRange(yaw, 315, 360))
            return invertIfNeed(invert, MathUtils.nearest(yaw, 315, 360));
        return yaw;
    }

    public static int getNearestPitchAxis(Entity entity) {
        return getNearestPitchAxis((int) entity.getPitch());
    }

    public static int getNearestPitchAxis(int pitch) {
        if (MathUtils.hasInRange(pitch, 45, 90))
            return MathUtils.nearest(pitch, 45, 90);
        if (MathUtils.hasInRange(pitch, 0, 45))
            return MathUtils.nearest(pitch, 0, 45);
        if (MathUtils.hasInRange(pitch, -45, 0))
            return MathUtils.nearest(pitch, -45, 0);
        if (MathUtils.hasInRange(pitch, -90, -45))
            return MathUtils.nearest(pitch, -90, -45);
        return pitch;
    }

    private static int invertIfNeed(boolean invert, int value) {
        if (invert) return -value;
        return value;
    }
}
