package com.ferra13671.BThack.core.Render.Utils;

import com.ferra13671.BThack.shaders.CoreShaders;
import com.ferra13671.BThack.api.Utils.MathUtils;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public final class ColorUtils {
    public static final int BLACK = fastRGBA(0, 0, 0, 255);
    public static final int WHITE = fastRGBA(255, 255, 255, 255);
    public static final int TRANSPARENT = fastRGBA(0, 0, 0, 0);
    public static final int RED = fastRGBA(255, 0, 0, 255);
    public static final int GREEN = fastRGBA(0, 255, 0, 255);

    public static int rainbow(long time) {
        double rainbowState = Math.ceil(time / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.5f, 1f).getRGB();
    }

    public static int rainbow() {
        return rainbow(1, 1);
    }

    public static int rainbow(int count, float speed) {
        double rainbowState = Math.ceil(((CoreShaders.shaderTicker.getPassedTime() * speed) + (100 * count)) / 20.0);
        rainbowState = (rainbowState % 360) / 360;
        return Color.getHSBColor((float) rainbowState, 0.5f, 1f).getRGB();
    }

    public static int gradient(int color1, int color2, int count, float scale, float speed) {
        float colorState = (float) Math.ceil(((CoreShaders.shaderTicker.getPassedTime() * speed) + ((200 * scale) * count)) / 20.0);
        colorState %= 360;
        colorState /= 360;
        if (colorState > 0.5) colorState = 1f - colorState;
        colorState *= 2f;

        float[] rgba1 = hashCodeToRGBA(color1);
        float[] rgba2 = hashCodeToRGBA(color2);

        return new Color(
                MathUtils.applyRange(MathHelper.lerp(colorState, rgba1[0], rgba2[0]), 0, 1),
                MathUtils.applyRange(MathHelper.lerp(colorState, rgba1[1], rgba2[1]), 0, 1),
                MathUtils.applyRange(MathHelper.lerp(colorState, rgba1[2], rgba2[2]), 0, 1),
                MathUtils.applyRange(MathHelper.lerp(colorState, rgba1[3], rgba2[3]), 0, 1)
        ).hashCode();
    }

    public static Color gradient(Color color1, Color color2, int count, float scale, float speed) {
        float colorState = (float) Math.ceil(((CoreShaders.shaderTicker.getPassedTime() * speed) + ((200 * scale) * count)) / 20.0);
        colorState %= 360;
        colorState /= 360;
        if (colorState > 0.5) colorState = 1f - colorState;
        colorState *= 2f;

        return new Color(
                MathUtils.applyRange(MathHelper.lerp(colorState, color1.getRed(), color2.getRed()), 0, 255),
                MathUtils.applyRange(MathHelper.lerp(colorState, color1.getGreen(), color2.getGreen()), 0, 255),
                MathUtils.applyRange(MathHelper.lerp(colorState, color1.getBlue(), color2.getBlue()), 0, 255),
                MathUtils.applyRange(MathHelper.lerp(colorState, color1.getAlpha(), color2.getAlpha()), 0, 255));
    }

    public static int integrateAlpha(int colorHashcode, int alpha) {
        int red = (colorHashcode >> 16 & 255);
        int green = (colorHashcode >> 8 & 255);
        int blue = (colorHashcode & 255);

        return fastRGBA(red, green, blue, alpha);
    }

    public static float[] hashCodeToRGB(int hashCode) {
        return new float[]{
                (float) ColorHelper.getRed(hashCode) / 255.0F,
                (float) ColorHelper.getGreen(hashCode) / 255.0F,
                (float) ColorHelper.getBlue(hashCode) / 255.0F
        };
    }

    public static float[] hashCodeToRGBA(int hashCode) {
        return new float[]{
                (float) ColorHelper.getRed(hashCode) / 255.0F,
                (float) ColorHelper.getGreen(hashCode) / 255.0F,
                (float) ColorHelper.getBlue(hashCode) / 255.0F,
                (float) ColorHelper.getAlpha(hashCode) / 255.0F
        };
    }

    @SuppressWarnings("PointlessBitwiseExpression")
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
