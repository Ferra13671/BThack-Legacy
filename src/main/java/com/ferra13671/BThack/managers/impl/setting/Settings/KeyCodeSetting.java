package com.ferra13671.BThack.managers.impl.setting.Settings;

import com.ferra13671.BThack.gui.screen.clickgui.component.components.ModuleButton;
import com.ferra13671.BThack.gui.screen.clickgui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.gui.screen.clickgui.component.components.setting.settings.KeyCode;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.KeyboardUtils;
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
