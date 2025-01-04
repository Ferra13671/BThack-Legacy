package com.ferra13671.BThack.api.Managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.function.Supplier;

public class KeyCodeSetting extends Setting {

    private int keyValue = 0;


    public KeyCodeSetting(String name, Module module, Supplier<Boolean> dependence) {
        super(name, module, dependence);
    }

    public KeyCodeSetting(String name, Module module) {
        super(name, module, null);
    }

    public int getValue() {
        return keyValue;
    }

    public void setValue(int value) {
        keyValue = value;
    }

    public boolean isPressed() {
        return KeyboardUtils.isKeyDown(getValue());
    }


    @Override
    public void toDefault() {
        //Nothing
    }

    @Override
    public void load(JsonObject jsonObject, JsonElement jsonElement) {
        setValue(jsonElement.getAsInt());
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(getName(), new JsonPrimitive(getValue()));
    }
}
