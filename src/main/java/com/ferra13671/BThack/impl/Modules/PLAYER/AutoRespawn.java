package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.mixins.gui_and_hud.MixinDeathScreen;

/**
 * @see MixinDeathScreen
 */

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn",
                "lang.module.AutoRespawn",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }
}