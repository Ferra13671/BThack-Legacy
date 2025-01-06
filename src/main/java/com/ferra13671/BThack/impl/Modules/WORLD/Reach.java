package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class Reach extends Module {

    public final NumberSetting range = new NumberSetting("Range", this, 0.5, 0.1, 4, false);

    public Reach() {
        super("Reach",
                "lang.module.Reach",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                range
        );
    }
}
