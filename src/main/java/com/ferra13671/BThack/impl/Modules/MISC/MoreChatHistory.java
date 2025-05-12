package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "MoreChatHistory", description = "lang.module.MoreChatHistory", category = "MISC")
public class MoreChatHistory extends Module {

    public final NumberSetting size = new NumberSetting("Size", this, 5000, 150, 10000, true);
}
