package com.ferra13671.BThack.api.Utils;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public final class MathUtils {

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static double roundNumber(double number, int scale) {
        BigDecimal bigDecimal = new BigDecimal(number);
        bigDecimal = bigDecimal.setScale(scale, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }

    public static double getDistance(Vec3d from, Vec3d to) {
        float f = (float)(from.x - to.x);
        float g = (float)(from.y - to.y);
        float h = (float)(from.z - to.z);
        return MathHelper.sqrt(f * f + g * g + h * h);
    }

    public static float absDegrees(float degrees) {
        degrees /= 360;
        degrees -= (int) degrees;
        degrees *= 360;
        if (degrees < 0) {
            return 360 - degrees;
        }
        return degrees;
    }

    public static int mirrorNumber(int minValue, int value, int maxValue) {
        if (value == maxValue)
            return minValue;
        if (value == minValue)
            return maxValue;
        if (value > maxValue) {
            return minValue - (value - maxValue);
        }
        if (value < minValue) {
            return maxValue + (minValue - value);
        }

        minValue--;
        maxValue++;

        ArrayList<Integer> numbers = new ArrayList<>();
        ArrayList<Integer> revertedNumbers = new ArrayList<>();
        for (int i = minValue; i < maxValue + 1; i++) {
            numbers.add(i);
        }
        for (int i = numbers.size() - 1; i > 0; i--) {
            revertedNumbers.add(numbers.get(i));
        }
        return revertedNumbers.get(value);
    }

    public static int removeNumbers(int number, int numbers) {
        int temp = number;
        while (temp - numbers > 0) {
            temp -= numbers;
        }
        return temp;
    }

    public static int applyRange(int number, int min, int max) {
        return Math.min(Math.max(min, number), max);
    }

    public static double applyRange(double number, double min, double max) {
        return Math.min(Math.max(min, number), max);
    }

    public static float applyRange(float number, float min, float max) {
        return Math.min(Math.max(min, number), max);
    }

    public static long applyRange(long number, long min, long max) {
        return Math.min(Math.max(min, number), max);
    }
}
