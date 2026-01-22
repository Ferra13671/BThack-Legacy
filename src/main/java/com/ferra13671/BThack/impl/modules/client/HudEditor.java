package com.ferra13671.BThack.impl.modules.client;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.module.OneActionModule;
import com.ferra13671.BThack.core.client.systems.gui.BThackScreens;

@ModuleInfo(name = "HudEditor", description = "lang.module.HudEditor", category = "CLIENT", visible = false, allowRemapVisible = false, allowRemapKeyCode = false)
public class HudEditor extends OneActionModule {

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
