package com.ferra13671.BThack.impl.Modules.WORLD;


import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class NoWeather extends Module {

    public NoWeather() {
        super("NoWeather",
                "lang.module.NoWeather",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

    }
}
