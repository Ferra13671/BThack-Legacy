package com.ferra13671.BThack.impl.Modules.PLAYER;


import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class BabyModel extends Module {
    public BabyModel() {
        super("BabyModel",
                "lang.module.BabyModel",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }
}
