package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class FogColor extends Module {

    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false);
    public final NumberSetting fogRed = new NumberSetting("Red", this, 255, 0, 255, false, () -> !rainbow.getValue());
    public final NumberSetting fogGreen = new NumberSetting("Green", this, 255, 0, 255, false, () -> !rainbow.getValue());
    public final NumberSetting fogBlue = new NumberSetting("Blue", this, 255, 0, 255, false, () -> !rainbow.getValue());

    public final BooleanSetting overworld = new BooleanSetting("Overworld", this, true);
    public final BooleanSetting nether = new BooleanSetting("Nether", this, true);
    public final BooleanSetting end = new BooleanSetting("End", this, true);

    public FogColor() {
        super("FogColor",
                "lang.module.FogColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

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
