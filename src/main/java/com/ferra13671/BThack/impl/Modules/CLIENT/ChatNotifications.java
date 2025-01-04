package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class ChatNotifications extends Module {

    public final BooleanSetting moduleToggle = new BooleanSetting("Module Toggle", this, false);
    public final BooleanSetting moduleMessages = new BooleanSetting("Module Messages", this, true);

    public ChatNotifications() {
        super("ChatNotifications",
                "lang.module.ChatNotifications",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        initSettings(
                moduleToggle,
                moduleMessages
        );
    }
}
