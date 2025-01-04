package com.ferra13671.BThack.api.Managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Supplier;

public class GuiButtonSetting extends Setting {

    private final Supplier<Screen> screenValue;

    public GuiButtonSetting(String name, Module module, Supplier<Screen> screen, Supplier<Boolean> dependence) {
        super(name, module, dependence);

        this.screenValue = screen;
    }

    public GuiButtonSetting(String name, Module module, Supplier<Screen> screen) {
        this(name, module, screen, null);
    }

    public Screen getValue() {
        return screenValue.get();
    }

    @Override
    public void toDefault() {
        //nothing
    }

    @Override
    public void load(JsonObject jsonObject, JsonElement jsonElement) {
        //nothing
    }

    @Override
    public void save(JsonObject jsonObject) {
        //nothing
    }
}
