package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class NoPush extends Module {

    public final BooleanSetting blocks = new BooleanSetting("Blocks", this, true);
    public final BooleanSetting entities = new BooleanSetting("Entities", this, false);
    public final BooleanSetting liquids = new BooleanSetting("Liquids", this, true);

    public NoPush() {
        super("NoPush",
                "lang.module.NoPush",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                blocks,
                entities,
                liquids
        );
    }
}
