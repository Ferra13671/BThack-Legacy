package com.ferra13671.BThack.api.Managers.managers;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.DisconnectEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Shader.ShaderTicker;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.ferra13671.BThack.api.Shader.MainMenu.MainMenuShader;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class MainMenuShaderManager implements Initializable, Mc {
    private final ShaderTicker shaderTicker = new ShaderTicker();
    private MainMenuShader shader;

    public MainMenuShaderManager() {}

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
        BThack.debug("Main Menu Shader Manager inited!");
    }

    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        if (Module.nullCheck()) return;
        resetShaderTime();
    }

    public void setMainMenuShader(MainMenuShader shader) {
        if (shader == null) return;
        if (this.shader != shader) {
            this.shader = shader;
            resetShaderTime();
        }
    }

    public MainMenuShader getMainMenuShader() {
        return shader;
    }

    public void resetShaderTime() {
        shaderTicker.reset();
        if (ModuleList.menuShader.isEnabled())
            Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(ModuleList.menuShader.getShader());
    }

    public void update() {
        shaderTicker.update(ModuleList.menuShader.speed.getValue().floatValue());
    }

    public float getShaderTime() {
        return shaderTicker.getPassedTime() / 1000f;
    }
}
