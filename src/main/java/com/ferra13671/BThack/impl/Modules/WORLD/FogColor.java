package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class FogColor extends Module {

    public static NumberSetting fogRed;
    public static NumberSetting fogGreen;
    public static NumberSetting fogBlue;
    public static BooleanSetting rainbow;

    public static BooleanSetting overworld;
    public static BooleanSetting nether;
    public static BooleanSetting end;

    public FogColor() {
        super("FogColor",
                "lang.module.FogColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        fogRed = new NumberSetting("Red", this, 255, 0, 255, false, () -> !rainbow.getValue());
        fogGreen = new NumberSetting("Green", this, 255, 0, 255, false, () -> !rainbow.getValue());
        fogBlue = new NumberSetting("Blue", this, 255, 0, 255, false, () -> !rainbow.getValue());
        rainbow = new BooleanSetting("Rainbow", this, false);

        overworld = new BooleanSetting("Overworld", this, true);
        nether = new BooleanSetting("Nether", this, true);
        end = new BooleanSetting("End", this, true);

        initSettings(
                fogRed,
                fogGreen,
                fogBlue,
                rainbow,
                overworld,
                nether,
                end
        );
    }
}
