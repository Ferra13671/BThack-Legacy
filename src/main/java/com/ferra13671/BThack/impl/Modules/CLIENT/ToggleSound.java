package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class ToggleSound extends Module {
    public static NumberSetting volume;

    public ToggleSound() {
        super("ToggleSound",
                "lang.module.ToggleSound",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                false
        );

        allowRemapKeyCode = false;

        volume = new NumberSetting("Volume", this, 0.25, 0.1, 1, false);

        initSettings(
                volume
        );
    }
}
