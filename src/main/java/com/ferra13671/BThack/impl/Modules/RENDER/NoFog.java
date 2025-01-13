package com.ferra13671.BThack.impl.Modules.RENDER;


import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class NoFog extends Module {

    public NoFog() {
        super("NoFog",
                "lang.module.NoFog",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );
    }
}
