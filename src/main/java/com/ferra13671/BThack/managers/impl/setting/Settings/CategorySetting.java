package com.ferra13671.BThack.managers.impl.setting.Settings;

import com.ferra13671.BThack.gui.screen.clickgui.component.components.ModuleButton;
import com.ferra13671.BThack.gui.screen.clickgui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.gui.screen.clickgui.component.components.setting.settings.CategoryButton;
import com.ferra13671.BThack.api.module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

//The value of this setting is the list of sub-settings assigned to this setting(category)
public class CategorySetting extends Setting<List<Setting<?>>> {

    public CategorySetting(String name, Module module, Supplier<Boolean> dependence) {
        super(name, module, new ArrayList<>(), dependence);
    }

    public CategorySetting(String name, Module module) {
        this(name, module, null);
    }

    @Override
    public void load(JsonObject jsonObject, JsonElement jsonElement) {
        JsonObject categoryObject = jsonElement.getAsJsonObject();
        getValue().forEach(setting -> {
            JsonElement settingValueObject = categoryObject.get(setting.getName());

            if (settingValueObject != null)
                setting.load(categoryObject, settingValueObject);
        });
    }

    @Override
    public void save(JsonObject jsonObject) {
        JsonObject categoryObject = new JsonObject();
        getValue().forEach(setting -> setting.save(categoryObject));
        jsonObject.add(getName(), categoryObject);
    }

    @Override
    public AbstractSetting<? extends Setting<List<Setting<?>>>> asSettingButton(ModuleButton parent, int offset) {
        return new CategoryButton(this, parent, offset, module);
    }
}
