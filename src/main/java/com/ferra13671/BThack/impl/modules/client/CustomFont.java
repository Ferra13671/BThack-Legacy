package com.ferra13671.BThack.impl.modules.client;

import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.impl.hud.ArrayListComponent;

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
