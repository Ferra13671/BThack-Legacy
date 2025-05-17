package com.ferra13671.BThack.api.Shaders;

import com.ferra13671.BThack.api.Utils.Ticker;

public class ShaderTicker {
    private final Ticker ticker = new Ticker();
    private long passedTime = 0;

    public ShaderTicker() {
        ticker.reset();
    }

    public void reset() {
        passedTime = 0;
        ticker.reset();
    }

    public void update(float speed) {
        passedTime += (long) (ticker.getPassedTime() * speed);
        ticker.reset();
    }

    public long getPassedTime() {
        return passedTime;
    }
}
