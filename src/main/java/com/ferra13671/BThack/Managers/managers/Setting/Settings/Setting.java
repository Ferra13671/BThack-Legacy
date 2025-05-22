package com.ferra13671.BThack.managers.managers.Setting.Settings;

import com.ferra13671.BThack.gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Supplier;

public abstract class Setting<T> {
    private final String name;
    public final Module module;

    public T value;
    public final T defaultValue;

    public final Supplier<Boolean> dependence;
    private boolean inCategory = false;

    protected Setting(String name, Module module, T value, Supplier<Boolean> dependence) {
        this.name = name;
        this.module = module;
        this.value = defaultValue = value;
        this.dependence = dependence;
    }


    public String getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    public T getValue() {
        return value;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void setValue(T value) {
        this.value = value;
        module.onChangeSetting(this);
    }

    public void toDefault() {
        value = defaultValue;
        module.onChangeSetting(this);
    }

    public boolean isInCategory() {
        return inCategory;
    }

    public abstract void load(JsonObject jsonObject, JsonElement jsonElement);

    public abstract void save(JsonObject jsonObject);

    public abstract AbstractSetting<? extends Setting<T>> asSettingButton(ModuleButton parent, int offset);

    public <S extends Setting<T>> S inCategory(CategorySetting categorySetting) {
        categorySetting.getValue().add(this);
        inCategory = true;
        return (S) this;
    }
}
