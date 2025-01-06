package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class NoBreakDelay extends Module {
    public static BooleanSetting noInstant;

    public NoBreakDelay() {
        super("NoBreakDelay",
                "lang.module.NoBreakDelay",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        noInstant = new BooleanSetting("No Instant", this, true);

        initSettings(
                noInstant
        );
    }
}
