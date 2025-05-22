package com.ferra13671.BThack.impl.Modules.Client;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.shaders.MainMenu.MainMenuBThackShader;
import com.ferra13671.BThack.shaders.MainMenu.MainMenuShaders;
import com.ferra13671.BThack.api.Utils.MathUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleInfo(name = "MenuShader", description = "lang.module.MenuShader", category = "CLIENT", autoEnabled = true, visible = false, allowRemapVisible = false, allowRemapKeyCode = false)
public class MenuShader extends Module {

    public final BooleanSetting random = new BooleanSetting("Random", this, false);
    public final ModeSetting shader = new ModeSetting("Sh", this, getShaderList(), () -> !random.getValue()).defaultValue("bubble");

    public final NumberSetting speed = new NumberSetting("Speed", this, 1, 0.1, 3, false);


    public List<String> getShaderList() {
        List<String> shaderNames = new ArrayList<>();
        MainMenuShaders.getShaders().forEach((name, shader) -> shaderNames.add(name));
        shaderNames.sort(Comparator.comparingInt(string -> (int) string.charAt(0)));
        return shaderNames;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onChangeSetting(Setting<?> setting) {
        Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(getShader());
    }

    public MainMenuBThackShader getShader() {
        return random.getValue() ?
                MainMenuShaders.getShaders().get(shader.getOptions().get(MathUtils.randomInt(0, MainMenuShaders.getShaders().size() - 1))) :
                MainMenuShaders.getShaders().get(shader.getValue());
    }
}
