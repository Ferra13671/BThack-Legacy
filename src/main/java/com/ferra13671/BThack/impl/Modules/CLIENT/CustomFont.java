package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.HudComponents.ArrayListComponent;

public class CustomFont extends Module {

    public CustomFont() {
        super("CustomFont",
                "lang.module.CustomFont",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );
    }

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
