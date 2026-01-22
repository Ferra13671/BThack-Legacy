package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;

@ModuleInfo(name = "MoreChatHistory", description = "lang.module.MoreChatHistory", category = "MISC")
public class MoreChatHistory extends Module {

    public final NumberSetting size = new NumberSetting("Size", this, 5000, 150, 10000, true);
}
