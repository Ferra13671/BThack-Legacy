package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

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
