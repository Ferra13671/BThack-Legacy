package com.ferra13671.BThack.api.Managers.managers.Setting.Settings;

import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.ColorPicker;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.awt.*;
import java.util.function.Supplier;

public class ColorSetting extends Setting<Color> {
    private boolean blockedAlpha = false;

    public ColorSetting(String name, Module module, Color defaultColor) {
        super(name, module, defaultColor, null);
    }

    public ColorSetting(String name, Module module, Color defaultColor, Supplier<Boolean> dependence) {
        super(name, module, defaultColor, dependence);
    }

    public Color getBrighterValue() {
        Color temp = getValue();
        return new Color(Math.min(255, temp.getRed() + 35), Math.min(255, temp.getGreen() + 35), Math.min(255, temp.getBlue() + 35), temp.getAlpha());
    }

    @Override
    public void load(JsonObject jsonObject, JsonElement jsonElement) {
        JsonObject colorObject = jsonElement.getAsJsonObject();
        if (colorObject.get("Red") != null && colorObject.get("Green") != null && colorObject.get("Blue") != null && colorObject.get("Alpha") != null) {
            Color color = new Color(
                    colorObject.get("Red").getAsInt(),
                    colorObject.get("Green").getAsInt(),
                    colorObject.get("Blue").getAsInt(),
                    colorObject.get("Alpha").getAsInt()
            );
            setValue(color);
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        JsonObject colorObject = new JsonObject();
        colorObject.add("Red", new JsonPrimitive(value.getRed()));
        colorObject.add("Green", new JsonPrimitive(value.getGreen()));
        colorObject.add("Blue", new JsonPrimitive(value.getBlue()));
        colorObject.add("Alpha", new JsonPrimitive(value.getAlpha()));

        jsonObject.add(getName(), colorObject);
    }

    public boolean isBlockedAlpha() {
        return blockedAlpha;
    }

    public ColorSetting withBlockedAlpha() {
        blockedAlpha = true;
        return this;
    }

    @Override
    public AbstractSetting<ColorSetting> asSettingButton(ModuleButton parent, int offset) {
        return new ColorPicker(this, parent, offset, module);
    }
}
