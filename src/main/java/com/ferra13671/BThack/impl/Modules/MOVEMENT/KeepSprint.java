package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class KeepSprint extends Module {

    public KeepSprint() {
        super("KeepSprint",
                "lang.module.KeepSprint",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }
}
