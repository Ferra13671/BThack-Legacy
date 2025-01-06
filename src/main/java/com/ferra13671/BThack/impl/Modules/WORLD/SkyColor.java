package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;


public class SkyColor extends Module {

    public static NumberSetting skyRed;
    public static NumberSetting skyGreen;
    public static NumberSetting skyBlue;

    public SkyColor() {
        super("SkyColour",
                "lang.module.SkyColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        skyRed = new NumberSetting("Red", this, 21, 0, 255, true);
        skyGreen = new NumberSetting("Green", this, 191, 0, 255, true);
        skyBlue = new NumberSetting("Blue", this, 219, 0, 255, true);

        initSettings(
                skyRed,
                skyGreen,
                skyBlue
        );
    }
}
