package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class CloudsColor extends Module {

    public static NumberSetting cloudsRed;
    public static NumberSetting cloudsGreen;
    public static NumberSetting cloudsBlue;

    public CloudsColor() {
        super("CloudsColor",
                "lang.module.CloudsColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        cloudsRed = new NumberSetting("Red", this, 255, 0, 255, false);
        cloudsGreen = new NumberSetting("Green", this, 255, 0, 255, false);
        cloudsBlue = new NumberSetting("Blue", this, 255, 0, 255, false);

        initSettings(
                cloudsRed,
                cloudsGreen,
                cloudsBlue
        );
    }
}
