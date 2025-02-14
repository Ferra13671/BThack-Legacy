package com.ferra13671.BThack.api.Managers.managers;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.DisconnectEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.ferra13671.BThack.api.Shader.MainMenu.MainMenuShader;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class MainMenuShaderManager implements Initializable, Mc {
    private final Ticker shaderTicker = new Ticker();
    private long passedTime = 0;
    private MainMenuShader shader;
    private Runnable postResetAction;

    public MainMenuShaderManager() {
        shaderTicker.reset();
    }

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
        BThack.debug("Main Menu Shader Manager inited!");
    }

    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        if (mc.player == null || mc.world == null) return;
        resetShaderTime();
    }

    public void setMainMenuShader(MainMenuShader shader) {
        if (shader == null) return;
        if (this.shader != shader) {
            this.shader = shader;
            resetShaderTime();
        }
    }

    public void setPostResetAction(Runnable runnable) {
        postResetAction = runnable;
    }

    public MainMenuShader getMainMenuShader() {
        return shader;
    }

    public void resetShaderTime() {
        shaderTicker.reset();
        passedTime = 0;
        if (postResetAction != null) postResetAction.run();
    }

    public void update() {
        passedTime += (long) (shaderTicker.getPassedTime() * ModuleList.menuShader.speed.getValue());
        shaderTicker.reset();
    }

    public float getShaderTime() {
        return passedTime / 1000f;
    }
}
