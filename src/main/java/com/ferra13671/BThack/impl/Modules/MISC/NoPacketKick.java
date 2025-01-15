package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class NoPacketKick extends Module {

    public final BooleanSetting chatNotify = new BooleanSetting("ChatNotify", this, false);

    public NoPacketKick() {
        super("NoPacketKick",
                "lang.module.NoPacketKick",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                chatNotify
        );
    }
}
