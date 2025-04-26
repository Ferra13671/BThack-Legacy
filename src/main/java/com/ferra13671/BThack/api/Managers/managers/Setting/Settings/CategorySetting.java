package com.ferra13671.BThack.api.Managers.managers.Setting.Settings;

import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.CategoryButton;
import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

//The value of this setting is the list of sub-settings assigned to this setting(category)
public class CategorySetting extends Setting<List<Setting<?>>> {
    protected CategorySetting(String name, Module module, Supplier<Boolean> dependence) {
        super(name, module, new ArrayList<>(), dependence);
    }

    @Override
    public void load(JsonObject jsonObject, JsonElement jsonElement) {

    }

    @Override
    public void save(JsonObject jsonObject) {

    }

    @Override
    public AbstractSetting<? extends Setting<List<Setting<?>>>> asSettingButton(ModuleButton parent, int offset) {
        return new CategoryButton(this, parent, offset, module);
    }
}
