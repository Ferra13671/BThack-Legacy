package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class BThackMainMenu extends Module {

    public final BooleanSetting screenChangeAnimation = new BooleanSetting("Screen Change Animation", this, true);
    public final NumberSetting animationSpeed = new NumberSetting("Anim. Speed", this, 1, 0.5, 3, false);

    public BThackMainMenu() {
        super("BThackMainMenu",
                "lang.module.BThackMainMenu",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        allowRemapKeyCode = false;
        allowRemapVisible = false;
        visible = false;

        initSettings(
                screenChangeAnimation,
                animationSpeed
        );
    }
}
