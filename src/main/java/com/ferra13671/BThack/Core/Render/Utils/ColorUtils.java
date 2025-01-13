package com.ferra13671.BThack.Core.Render.Utils;

import com.ferra13671.BThack.api.Utils.MathUtils;

import java.awt.*;

public final class ColorUtils {
    public static final int BLACK = fastRGBA(0, 0, 0, 255);
    public static final int WHITE = fastRGBA(255, 255, 255, 255);
    public static final int TRANSPARENT = fastRGBA(0, 0, 0, 0);
    public static final int RED = fastRGBA(255, 0, 0, 255);
    public static final int GREEN = fastRGBA(0, 255, 0, 255);


    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.5f, 1f).getRGB();
    }

    public static int rainbow(int delay, float speed) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        float rSpeed = 360 * speed;
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / rSpeed), 0.5f, 1f).getRGB();
    }

    public static int rainbowType(int type) {
        float speed = RainbowUtils.getRainbowSpeed(type)[0];
        int delay = (int)RainbowUtils.getRainbowSpeed(type)[1];

        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        float rSpeed = 360 * speed;
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / rSpeed), 0.5f, 1f).getRGB();
    }

    public static int rainbowType(int type, float counter) {
        float speed = RainbowUtils.getRainbowSpeed(type)[0];
        int delay = (int)RainbowUtils.getRainbowSpeed(type)[1];

        delay = (int)(delay * counter);

        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        float rSpeed = 360 * speed;
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / rSpeed), 0.5f, 1f).getRGB();
    }

    public static int integrateAlpha(int colorHashcode, int alpha) {
        int red = (colorHashcode >> 16 & 255);
        int green = (colorHashcode >> 8 & 255);
        int blue = (colorHashcode & 255);

        return fastRGBA(red, green, blue, alpha);
    }

    public static int fastRGBA(int red, int green, int blue, int alpha) {
        return ((MathUtils.applyRange(alpha, 0, 255) & 0xFF) << 24) |
                ((MathUtils.applyRange(red, 0, 255) & 0xFF) << 16) |
                ((MathUtils.applyRange(green, 0, 255) & 0xFF) << 8)  |
                ((MathUtils.applyRange(blue, 0, 255) & 0xFF) << 0);
    }

    public static int fastRGBA(int rgb) {
        return 0xff000000 | rgb;
    }
}
