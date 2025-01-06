package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class WorldElements extends Module {

    public static NumberSetting starBrightness;
    public static BooleanSetting changeMoonPhase;
    public static NumberSetting moonPhase;

    public WorldElements() {
        super("WorldElements",
                "lang.module.WorldElements",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        starBrightness = new NumberSetting("Star Bright.", this, 0.5,0,1, false);
        changeMoonPhase = new BooleanSetting("Ch. Moon Phase", this, false);
        moonPhase = new NumberSetting("Moon Phase", this, 5, 0, 7, true, changeMoonPhase::getValue);

        initSettings(
                starBrightness,
                changeMoonPhase,
                moonPhase
        );
    }
}
