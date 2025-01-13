package com.ferra13671.BTbot.api.Utils.Generate;

import java.util.Random;

public class GenerateNumber {
    private static final Random random = new Random();

    public static int generateInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static float generateFloat(float min, float max) {
        return (float) Math.min(max, ( Math.random() * ((max * 1.1) - min) ) + min);
    }

    public static double generateDouble(double min, double max) {
        return Math.min(max, ( Math.random() * ((max * 1.1) - min) ) + min);
    }
}
