package com.ferra13671.BThack.api.Utils;

public final class Ticker {
    private long time;

    public Ticker() {
        time = -1;
    }

    public float getPassedTime() {
        return System.currentTimeMillis() - this.time;
    }

    public boolean passed(double ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }

    public void reset(long offset) {
        time = System.currentTimeMillis() + offset;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}