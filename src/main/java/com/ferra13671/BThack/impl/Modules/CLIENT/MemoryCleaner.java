package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class MemoryCleaner extends Module {

    public final BooleanSetting showMessages = new BooleanSetting("Show Messages", this, true);

    public MemoryCleaner() {
        super("MemoryCleaner",
                "lang.module.MemoryCleaner",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        allowRemapKeyCode = false;
        allowRemapVisible = false;
        setVisible(false);

        initSettings(showMessages);
    }
}
