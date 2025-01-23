package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class CustomFont extends Module {

    public CustomFont() {
        super("CustomFont",
                "lang.module.CustomFont",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );
    }
}
