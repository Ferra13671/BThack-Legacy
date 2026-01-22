package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;

@ModuleInfo(name = "NoPacketKick", description = "lang.module.NoPacketKick", category = "MISC")
public class NoPacketKick extends Module {

    public final BooleanSetting chatNotify = new BooleanSetting("ChatNotify", this, false);
}
