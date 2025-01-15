package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;


public class SkyColor extends Module {

    public final NumberSetting skyRed = new NumberSetting("Red", this, 21, 0, 255, true);
    public final NumberSetting skyGreen = new NumberSetting("Green", this, 191, 0, 255, true);
    public final NumberSetting skyBlue = new NumberSetting("Blue", this, 219, 0, 255, true);

    public SkyColor() {
        super("SkyColour",
                "lang.module.SkyColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                skyRed,
                skyGreen,
                skyBlue
        );
    }
}
