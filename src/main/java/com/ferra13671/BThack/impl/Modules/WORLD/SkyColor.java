package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

import java.awt.*;


public class SkyColor extends Module {

    public final ColorSetting skyColor = new ColorSetting("Sky Color", this, new Color(21, 191, 219)).withBlockedAlpha();

    public SkyColor() {
        super("SkyColour",
                "lang.module.SkyColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                skyColor
        );
    }
}
