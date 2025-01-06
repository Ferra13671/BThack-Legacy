package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class MoreChatHistory extends Module {

    public static NumberSetting size;

    public MoreChatHistory() {
        super(  "MoreChatHistory",
                "lang.module.MoreChatHistory",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        size = new NumberSetting("Size", this, 5000, 150, 10000, true);

        initSettings(
                size
        );
    }
}
