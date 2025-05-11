package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class NoBreakDelay extends Module {

    public final BooleanSetting noInstant = new BooleanSetting("No Instant", this, true);

    public NoBreakDelay() {
        super("NoBreakDelay",
                "lang.module.NoBreakDelay",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }
}
