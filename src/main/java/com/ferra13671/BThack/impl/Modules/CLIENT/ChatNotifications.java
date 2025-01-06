package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class ChatNotifications extends Module {

    public static BooleanSetting moduleToggle;
    public static BooleanSetting moduleMessages;

    public ChatNotifications() {
        super("ChatNotifications",
                "lang.module.ChatNotifications",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        moduleToggle = new BooleanSetting("Module Toggle", this, false);
        moduleMessages = new BooleanSetting("Module Messages", this, true);

        initSettings(
                moduleToggle,
                moduleMessages
        );
    }
}
