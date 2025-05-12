package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.GuiSystem.BThackScreens;

@ModuleInfo(name = "HudEditor", description = "lang.module.HudEditor", category = "CLIENT")
public class HudEditor extends OneActionModule {

    public HudEditor() {
        allowRemapKeyCode = false;
        allowRemapVisible = false;
    }

    @Override
    public void playOnSound() {}

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        mc.setScreen(BThackScreens.HUD_EDITOR);
    }
}
