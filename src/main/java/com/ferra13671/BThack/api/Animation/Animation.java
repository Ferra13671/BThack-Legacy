package com.ferra13671.BThack.api.Animation;

import com.ferra13671.BThack.api.Utils.Mc;

public class Animation implements Mc, Cloneable {
    private final Easing easing;
    private int millis;

    private long startMillis;


    public Animation(Easing easing, int millis) {
        this.easing = easing;
        this.millis = millis;

        startMillis = System.currentTimeMillis();
    }

    public void reset() {
        startMillis = System.currentTimeMillis();
    }

    public void setStartMillis(long millis) {
        startMillis = millis;
    }

    public double getEase() {
        long currentMillis = getPassedMillis();
        return currentMillis >= millis ? 1 : easing.ease(currentMillis / (double) millis);
    }

    public int getMillis() {
        return millis;
    }

    public void setMillis(int millis) {
        this.millis = millis;
    }

    public long getPassedMillis() {
        return System.currentTimeMillis() - startMillis;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Animation clone() {
        return new Animation(easing, millis);
    }
}
