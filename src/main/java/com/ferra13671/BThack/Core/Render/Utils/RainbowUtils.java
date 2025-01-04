package com.ferra13671.BThack.Core.Render.Utils;

public final class RainbowUtils {
    public static float[] getRainbowSpeed(int rainbowType) {

        int delay;
        float speed;
        delay = switch (rainbowType) {
            default -> {
                speed = 1;
                yield 100;
            }
            case 2 -> {
                speed = 1;
                yield 200;
            }
            case 3 -> {
                speed = 1;
                yield 300;
            }
            case 4 -> {
                speed = 1;
                yield 500;
            }
            case 5 -> {
                speed = 0.1f;
                yield 750;
            }
            case 6 -> {
                speed = 0.1f;
                yield 700;
            }
            case 7 -> {
                speed = 0.1f;
                yield 600;
            }
            case 8 -> {
                speed = 0.1f;
                yield 850;
            }
        };
        return new float[] {speed, delay};
    }

    public static float[] getRainbowRectSpeed(int rainbowType) {
        int delay;
        float speed;

        delay = switch (rainbowType) {
            default -> {
                speed = 1;
                yield 40;
            }
            case 2 -> {
                speed = 0.5f;
                yield 20;
            }
            case 3 -> {
                speed = 0.2f;
                yield 10;
            }
            case 4 -> {
                speed = 0.1f;
                yield 725;
            }
        };
        return new float[]{speed, delay};
    }
}
