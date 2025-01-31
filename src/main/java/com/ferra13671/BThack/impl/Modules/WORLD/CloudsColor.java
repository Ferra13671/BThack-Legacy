package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

import java.awt.*;

public class CloudsColor extends Module {

    public final ColorSetting cloudsColor = new ColorSetting("Clouds Color", this, new Color(255, 255, 255)).withBlockedAlpha();

    public CloudsColor() {
        super("CloudsColor",
                "lang.module.CloudsColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                cloudsColor
        );
    }
}
