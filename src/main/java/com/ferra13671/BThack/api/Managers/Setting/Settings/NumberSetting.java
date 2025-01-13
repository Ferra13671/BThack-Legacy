package com.ferra13671.BThack.api.Managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.function.Supplier;

public class NumberSetting extends Setting {

    private double numberValue;
    private final double defaultNumberValue;
    private final double minValue;
    private final double maxValue;
    public final boolean onlyInt;

    public NumberSetting(String name, Module module, double numberValue, double minValue, double maxValue, boolean onlyInt, Supplier<Boolean> dependence) {
        super(name, module, dependence);

        this.numberValue = defaultNumberValue = numberValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.onlyInt = onlyInt;
    }

    public NumberSetting(String name, Module module, double numberValue, double minValue, double maxValue, boolean onlyInt) {
        this(name, module, numberValue, minValue, maxValue, onlyInt, null);
    }

    public double getValue(){
        if(this.onlyInt){
            numberValue = (int)numberValue;
        }
        return numberValue;
    }

    public void setValue(double value) {
        numberValue = value;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }


    @Override
    public void toDefault() {
        numberValue = defaultNumberValue;
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
