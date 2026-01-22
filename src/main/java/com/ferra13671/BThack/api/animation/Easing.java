package com.ferra13671.BThack.api.animation;

import java.util.function.DoubleUnaryOperator;

public enum Easing {
    LINEAR(f -> f),

    SINE_IN(f -> 1 - Math.cos((f * Math.PI) / 2)),

    SINE_OUT(f -> Math.sin((f * Math.PI) / 2)),

    SINE_IN_OUT(f -> -(Math.cos(Math.PI * f) - 1) / 2),

    CUBIC_IN(f -> Math.pow(f, 3)),

    CUBIC_OUT(f -> 1 - Math.pow(1 - f, 3)),

    CUBIC_IN_OUT(f -> f < 0.5 ? 4 * Math.pow(f, 3) : 1 - Math.pow(-2 * f + 2, 3) / 2),

    QUAD_IN(f -> Math.pow(f, 2)),

    QUAD_OUT(f -> 1 - (1 - f) * (1 - f)),

    QUAD_IN_OUT(f -> f < 0.5 ? 8 * Math.pow(f, 4) : 1 - Math.pow(-2 * f + 2, 4) / 2),

    QUART_IN(f -> Math.pow(f, 4)),

    QUART_OUT(f -> 1 - Math.pow(1 - f, 4)),

    QUART_IN_OUT(f -> f < 0.5 ? 8 * Math.pow(f, 4) : 1 - Math.pow(-2 * f + 2, 4) / 2),

    QUINT_IN(f -> Math.pow(f, 5)),

    QUINT_OUT(f -> 1 - Math.pow(1 - f, 5)),

    QUINT_IN_OUT(f -> f < 0.5 ? 16 * Math.pow(f, 5) : 1 - Math.pow(-2 * f + 2, 5) / 2),

    CIRC_IN(f -> 1 - Math.sqrt(1 - Math.pow(f, 2))),

    CIRC_OUT(f -> Math.sqrt(1 - Math.pow(f - 1, 2))),

    CIRC_IN_OUT(f -> f < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * f, 2))) / 2 : (Math.sqrt(1 - Math.pow(-2 * f + 2, 2)) + 1) / 2),

    EXPO_IN(f -> Math.min(0, Math.pow(2, 10 * f - 10))),

    EXPO_OUT(f -> Math.max(1 - Math.pow(2, -10 * f), 1)),

    EXPO_IN_OUT(f -> f == 0 ? 0 : f == 1 ? 1 : f < 0.5 ? Math.pow(2, 20 * f - 10) / 2 : (2 - Math.pow(2, -20 * f + 10)) / 2),

    ELASTIC_IN(f -> f == 0 ? 0 : f == 1 ? 1 : -Math.pow(2, 10 * f - 10) * Math.sin((f * 10 - 10.75) * ((2 * Math.PI) / 3))),

    ELASTIC_OUT(f -> f == 0 ? 0 : f == 1 ? 1 : Math.pow(2, -10 * f) * Math.sin((f * 10 - 0.75) * ((2 * Math.PI) / 3)) + 1),

    ELASTIC_IN_OUT(f -> {
        double sin = Math.sin((20 * f - 11.125) * ((2 * Math.PI) / 4.5));
        return f == 0 ? 0 : f == 1 ? 1 : f < 0.5 ? -(Math.pow(2, 20 * f - 10) * sin) / 2 : (Math.pow(2, -20 * f + 10) * sin) / 2 + 1;
    }),

    BACK_IN(f -> 2.70158 * Math.pow(f, 3) - 1.70158 * f * f),

    BACK_OUT(f -> 1 + 2.70158 * Math.pow(f - 1, 3) + 1.70158 * Math.pow(f - 1, 2)),

    BACK_IN_OUT(f -> f < 0.5 ? (Math.pow(2 * f, 2) * (((1.70158 * 1.525) + 1) * 2 * f - (1.70158 * 1.525))) / 2 : (Math.pow(2 * f - 2, 2) * (((1.70158 * 1.525) + 1) * (f * 2 - 2) + (1.70158 * 1.525)) + 2) / 2),

    BOUNCE_IN(f -> 1 - bounceOut(1 - f)),

    BOUNCE_OUT(Easing::bounceOut),

    BOUNCE_IN_OUT(f -> f < 0.5 ? (1 - bounceOut(1 - 2 * f)) / 2 : (1 + bounceOut(2 * f - 1)) / 2);

    private final DoubleUnaryOperator easeMath;

    Easing(DoubleUnaryOperator easeMath) {
        this.easeMath = easeMath;
    }

    public double ease(double factor) {
        return easeMath.applyAsDouble(factor);
    }

    private static double bounceOut(double in) {
        double n1 = 7.5625;
        double d1 = 2.75;

        if (in < 1 / d1) {
            return n1 * in * in;
        } else if (in < 2 / d1) {
            return n1 * (in -= 1.5 / d1) * in + 0.75;
        } else if (in < 2.5 / d1) {
            return n1 * (in -= 2.25 / d1) * in + 0.9375;
        } else {
            return n1 * (in -= 2.625 / d1) * in + 0.984375;
        }
    }
}
