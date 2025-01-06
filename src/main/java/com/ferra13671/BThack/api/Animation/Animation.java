package com.ferra13671.BThack.api.Animation;

public class Animation {
    private final Easing easing;
    private final int millis;

    private long startMillis;


    public Animation(Easing easing, int millis) {
        this.easing = easing;
        this.millis = millis;

        startMillis = System.currentTimeMillis();
    }

    public void reset() {
        startMillis = System.currentTimeMillis();
    }

    public double getEase() {
        long currentMillis = getPassedMillis();
        return currentMillis >= millis ? 1 : easing.ease(currentMillis / (double) millis);
    }

    public int getMillis() {
        return millis;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public long getPassedMillis() {
        return System.currentTimeMillis() - startMillis;
    }
}
