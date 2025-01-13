package com.ferra13671.BThack.api.Managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {

    private boolean booleanValue;
    private final boolean defaultValue;


    public BooleanSetting(String name, Module module, boolean defaultValue, Supplier<Boolean> dependence) {
        super(name, module, dependence);

        this.booleanValue = this.defaultValue = defaultValue;
    }

    public BooleanSetting(String name, Module module, boolean defaultValue) {
        this(name, module, defaultValue, null);
    }

    public Boolean getValue() {
        return booleanValue;
    }

    public void setValue(boolean value) {
        this.booleanValue = value;
    }

    @Override
    public void toDefault() {
        booleanValue = defaultValue;
    }

    @Override
    public void load(JsonObject jsonObject, JsonElement jsonElement) {
        setValue(jsonElement.getAsBoolean());
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(getName(), new JsonPrimitive(getValue()));
    }
}
