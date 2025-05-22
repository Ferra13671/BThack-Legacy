package com.ferra13671.BThack.managers.managers;

import com.ferra13671.BThack.api.Utils.Initializable;

public class TickManager implements Initializable {
    private float tickModifier = 1;

    public float getTickModifier() {
        return tickModifier;
    }

    public void applyTickModifier(float modifier) {
        tickModifier = modifier;
    }

    public void applyTickModifierWithFactor(double factor) {
        tickModifier = (float) ((50f / factor) / 50);
    }

    @Override
    public void init() {
        //no action
    }
}
