package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BTbot.api.Utils.Generate.NumberGenerator;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shader.MainMenu.MainMenuShader;
import com.ferra13671.BThack.api.Shader.MainMenu.MainMenuShaders;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuShader extends Module {

    public final BooleanSetting random = new BooleanSetting("Random", this, false);
    public final ModeSetting shader = new ModeSetting("Sh", this, getShaderList(), () -> !random.getValue()).defaultValue("bubble");

    public final NumberSetting speed = new NumberSetting("Speed", this, 1, 0.1, 3, false);

    public MenuShader() {
        super("MenuShader",
                "lang.module.MenuShader",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        allowRemapVisible = false;
        setVisible(false);

        initSettings(
                random,
                shader,

                speed
        );

        Managers.MAIN_MENU_SHADER_MANAGER.setPostResetAction(() -> {
            if (this.isEnabled())
                Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(getShader());
        });
    }

    public List<String> getShaderList() {
        List<String> shaderNames = new ArrayList<>();
        MainMenuShaders.getShaders().forEach((name, shader) -> shaderNames.add(name.toLowerCase()));
        return shaderNames;
    }

    @Override
    public void onEnable() {
        Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(getShader());
    }

    @Override
    public void onChangeSetting(Setting setting) {
        if (this.isEnabled())
            Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(getShader());
    }

    public MainMenuShader getShader() {
        if (!random.getValue()) {
            return MainMenuShaders.getShaders().get(shader.getValue().toUpperCase());
        } else {
            int randomIShader = NumberGenerator.generateInt(0, MainMenuShaders.getShaders().size() - 1);
            return MainMenuShaders.getShaders().get(shader.getOptions().get(randomIShader).toUpperCase());
        }
    }
}
