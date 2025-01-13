package com.ferra13671.BThack.api.Managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.awt.*;
import java.util.function.Supplier;

@Deprecated
public class ColorSetting extends Setting {

    private Color color;
    private final Color defaultColor;

    public ColorSetting(String name, Module module, Color defaultColor) {
        super(name, module, null);
        this.defaultColor = color = defaultColor;
    }

    public ColorSetting(String name, Module module, Color defaultColor, Supplier<Boolean> dependence) {
        super(name, module, dependence);
        this.defaultColor = color = defaultColor;
    }

    @Override
    public void toDefault() {
        color = defaultColor;
    }

    public Color getValue() {
        return color;
    }

    public void setValue(Color color) {
        this.color = color;
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
        colorObject.add("Red", new JsonPrimitive(color.getRed()));
        colorObject.add("Green", new JsonPrimitive(color.getGreen()));
        colorObject.add("Blue", new JsonPrimitive(color.getBlue()));
        colorObject.add("Alpha", new JsonPrimitive(color.getAlpha()));

        jsonObject.add(getName(), colorObject);
    }
}
