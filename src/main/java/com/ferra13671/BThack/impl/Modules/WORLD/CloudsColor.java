package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class CloudsColor extends Module {

    public final NumberSetting cloudsRed = new NumberSetting("Red", this, 255, 0, 255, false);
    public final NumberSetting cloudsGreen = new NumberSetting("Green", this, 255, 0, 255, false);
    public final NumberSetting cloudsBlue = new NumberSetting("Blue", this, 255, 0, 255, false);

    public CloudsColor() {
        super("CloudsColor",
                "lang.module.CloudsColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                cloudsRed,
                cloudsGreen,
                cloudsBlue
        );
    }
}
