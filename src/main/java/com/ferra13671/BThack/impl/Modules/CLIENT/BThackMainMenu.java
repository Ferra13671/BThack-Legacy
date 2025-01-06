package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class BThackMainMenu extends Module {

    public static BooleanSetting playStartMusic;

    public BThackMainMenu() {
        super("BThackMainMenu",
                "lang.module.BThackMainMenu",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        playStartMusic = new BooleanSetting("Play Start Music", this, true);
    }
}
