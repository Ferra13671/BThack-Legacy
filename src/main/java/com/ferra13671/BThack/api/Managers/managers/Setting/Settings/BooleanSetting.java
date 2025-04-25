package com.ferra13671.BThack.api.Managers.managers.Setting.Settings;

import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.Checkbox;
import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.function.Supplier;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, Module module, boolean defaultValue, Supplier<Boolean> dependence) {
        super(name, module, defaultValue, dependence);
    }

    public BooleanSetting(String name, Module module, boolean defaultValue) {
        this(name, module, defaultValue, null);
    }

    @Override
    public void load(JsonObject jsonObject, JsonElement jsonElement) {
        setValue(jsonElement.getAsBoolean());
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(getName(), new JsonPrimitive(getValue()));
    }

    @Override
    public AbstractSetting<BooleanSetting> asSettingButton(ModuleButton parent, int offset) {
        return new Checkbox(this, parent, offset, module);
    }
}
