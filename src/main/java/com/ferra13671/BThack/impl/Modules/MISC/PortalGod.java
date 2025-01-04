package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class PortalGod extends Module {

    public PortalGod() {
        super("PortalGod",
                "lang.module.PortalGod",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }
}
