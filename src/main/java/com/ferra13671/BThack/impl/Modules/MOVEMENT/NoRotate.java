package com.ferra13671.BThack.impl.Modules.MOVEMENT;


import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class NoRotate extends Module {

    public final BooleanSetting blockPitch = new BooleanSetting("BlockPitchRotate", this, true);

    public NoRotate() {
        super("NoRotate",
                "lang.module.NoRotate",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                blockPitch
        );
    }
}
