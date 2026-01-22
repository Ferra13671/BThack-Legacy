package com.ferra13671.BThack.impl.modules.client;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;

@ModuleInfo(name = "CustomCape", description = "lang.module.CustomCape", category = "CLIENT", autoEnabled = true, visible = false, allowRemapVisible = false, allowRemapKeyCode = false)
public class CustomCape extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
        Managers.CAPE_MANAGER.setEnabled(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Managers.CAPE_MANAGER.setEnabled(false);
    }
}
