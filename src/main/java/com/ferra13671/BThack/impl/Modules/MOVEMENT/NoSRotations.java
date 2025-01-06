package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class NoSRotations extends Module {
    public NoSRotations() {
        super("NoSRotations",
                "lang.module.NoSRotations",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }
}
