package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class NoPush extends Module {

    public static BooleanSetting blocks;
    public static BooleanSetting entities;
    public static BooleanSetting liquids;

    public NoPush() {
        super("NoPush",
                "lang.module.NoPush",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        blocks = new BooleanSetting("Blocks", this, true);
        entities = new BooleanSetting("Entities", this, false);
        liquids = new BooleanSetting("Liquids", this, true);

        initSettings(
                blocks,
                entities,
                liquids
        );
    }
}
