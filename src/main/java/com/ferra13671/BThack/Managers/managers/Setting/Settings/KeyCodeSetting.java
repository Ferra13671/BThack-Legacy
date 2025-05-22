package com.ferra13671.BThack.managers.managers.Setting.Settings;

import com.ferra13671.BThack.gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.gui.Screen.ClickGui.component.components.setting.settings.KeyCode;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.function.Supplier;

public class KeyCodeSetting extends Setting<Integer> {

    public KeyCodeSetting(String name, Module module, Supplier<Boolean> dependence) {
        super(name, module, 0, dependence);
    }

    public KeyCodeSetting(String name, Module module) {
        this(name, module, null);
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

    @Override
    public AbstractSetting<KeyCodeSetting> asSettingButton(ModuleButton parent, int offset) {
        return new KeyCode(parent, offset, this, module);
    }
}
