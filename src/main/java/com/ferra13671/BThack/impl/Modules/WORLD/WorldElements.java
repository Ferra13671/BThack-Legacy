package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class WorldElements extends Module {

    public final NumberSetting starBrightness = new NumberSetting("Star Bright.", this, 0.5,0,1, false);
    public final BooleanSetting changeMoonPhase = new BooleanSetting("Ch. Moon Phase", this, false);
    public final NumberSetting moonPhase = new NumberSetting("Moon Phase", this, 5, 0, 7, true, changeMoonPhase::getValue);

    public WorldElements() {
        super("WorldElements",
                "lang.module.WorldElements",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                starBrightness,
                changeMoonPhase,
                moonPhase
        );
    }
}
