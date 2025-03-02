package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class ToggleSound extends Module {
    public final NumberSetting volume = new NumberSetting("Volume", this, 0.25, 0.1, 1, false);

    public ToggleSound() {
        super("ToggleSound",
                "lang.module.ToggleSound",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                false
        );

        allowRemapKeyCode = false;
        allowRemapVisible = false;
        setVisible(false);

        initSettings(
                volume
        );
    }
}
