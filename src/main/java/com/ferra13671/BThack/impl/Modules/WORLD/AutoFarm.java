package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

@Deprecated
public class AutoFarm extends Module {

    public AutoFarm() {
        super("AutoFarm",
                "",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );
    }
}
