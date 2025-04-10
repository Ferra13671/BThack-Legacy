package com.ferra13671.BTbot.api.Utils.Generate;

import com.ferra13671.BThack.Constants;

public class NumberGenerator {

    public static int generateInt(int min, int max) {
        return Constants.RANDOM.nextInt(max - min + 1) + min;
    }

    public static float generateFloat(float min, float max) {
        return (float) Math.min(max, (Constants.RANDOM.nextDouble() * ((max * 1.1) - min)) + min);
    }

    public static double generateDouble(double min, double max) {
        return Math.min(max, (Constants.RANDOM.nextDouble() * ((max * 1.1) - min)) + min);
    }
}
