package com.ferra13671.BThack.impl.Modules.Client;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.impl.HudComponents.ArrayListComponent;

@ModuleInfo(name = "CustomFont", description = "lang.module.CustomFont", category = "CLIENT", autoEnabled = true)
public class CustomFont extends Module {

    @Override
    public void onEnable() {
        ArrayListComponent.updateSizes();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        ArrayListComponent.updateSizes();
        super.onDisable();
    }
}
