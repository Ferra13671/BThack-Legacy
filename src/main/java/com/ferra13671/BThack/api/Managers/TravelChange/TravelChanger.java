package com.ferra13671.BThack.api.Managers.TravelChange;

import java.util.function.Supplier;

public class TravelChanger {
    public int priority;
    /** [0] - yaw   [1] - pitch */
    public final Supplier<Float[]> rotateGetter;
    public final Runnable preUpdateVelocityRunnable;
    public final Supplier<Boolean> needTravelChange;
    public boolean needRewriteTravelRot = true;

    public TravelChanger(int priority, Supplier<Float[]> rotateGetter, Runnable preUpdateVelocityRunnable, Supplier<Boolean> needTravelChange) {
        this.priority = priority;
        this.rotateGetter = rotateGetter;
        this.preUpdateVelocityRunnable = preUpdateVelocityRunnable;
        this.needTravelChange = needTravelChange;
    }

    public TravelChanger(int priority, Supplier<Float[]> rotateGetter) {
        this(priority, rotateGetter, () -> {}, () -> true);
    }
}
