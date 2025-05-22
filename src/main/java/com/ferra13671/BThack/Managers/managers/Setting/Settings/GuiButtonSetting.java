package com.ferra13671.BThack.managers.managers.Setting.Settings;

import com.ferra13671.BThack.gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.gui.Screen.ClickGui.component.components.setting.settings.OpenGuiButton;
import com.ferra13671.BThack.api.Module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Supplier;

public class GuiButtonSetting extends Setting<Supplier<Screen>> {

    public GuiButtonSetting(String name, Module module, Supplier<Screen> screen, Supplier<Boolean> dependence) {
        super(name, module, screen, dependence);
    }

    public GuiButtonSetting(String name, Module module, Supplier<Screen> screen) {
        this(name, module, screen, null);
    }

    @Override
    public void load(JsonObject jsonObject, JsonElement jsonElement) {
        //nothing
    }

    @Override
    public void save(JsonObject jsonObject) {
        //nothing
    }

    @Override
    public AbstractSetting<GuiButtonSetting> asSettingButton(ModuleButton parent, int offset) {
        return new OpenGuiButton(this, parent, offset, module);
    }
}
