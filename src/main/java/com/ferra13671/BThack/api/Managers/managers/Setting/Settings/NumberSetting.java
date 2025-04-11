package com.ferra13671.BThack.api.Managers.managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.function.Supplier;

public class NumberSetting extends Setting<Double> {
    private final double minValue;
    private final double maxValue;
    public final boolean onlyInt;

    public NumberSetting(String name, Module module, double numberValue, double minValue, double maxValue, boolean onlyInt, Supplier<Boolean> dependence) {
        super(name, module, numberValue, dependence);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.onlyInt = onlyInt;
    }

    public NumberSetting(String name, Module module, double numberValue, double minValue, double maxValue, boolean onlyInt) {
        this(name, module, numberValue, minValue, maxValue, onlyInt, null);
    }

    public Double getValue() {
        return onlyInt ? value.intValue() : value;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    @Override
    public void load(JsonObject jsonObject, JsonElement jsonElement) {
        setValue(jsonElement.getAsDouble());
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(getName(), new JsonPrimitive(getValue()));
    }
}
