package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "ChatNotifications", description = "lang.module.ChatNotifications", category = "CLIENT", autoEnabled = true, visible = false, allowRemapVisible = false, allowRemapKeyCode = false)
public class ChatNotifications extends Module {
    public final BooleanSetting moduleToggle = new BooleanSetting("Module Toggle", this, false);
    public final BooleanSetting moduleMessages = new BooleanSetting("Module Messages", this, true);
}
